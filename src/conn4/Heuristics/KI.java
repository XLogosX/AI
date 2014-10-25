/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package conn4.Heuristics;

import java.awt.EventQueue;
import java.util.concurrent.locks.ReentrantLock;

public abstract class KI {
        private int listener;
        protected int field;
        protected int lastCol;
        
        public KI() {
                thread.start();
        }
        
        public void requestMove(int field, int listener, int lastCol) {
                this.field = field;
                this.listener = listener;
                this.lastCol = lastCol;
                
                startLock.lock();
                start = true;
                startLock.unlock();
        }
        
        protected abstract void requestMove();
        
        protected void move(int col) {
                moveCol = col;
                EventQueue.invokeLater(kiMove);
        }
        
        private Runnable kiMove = new Runnable() {
                public void run() {
                //listener.move(moveCol);
                }
        };
        
        @Override
        protected void finalize() {
                stop();
        }
        
        public void stop() {
                stopLock.lock();
                stop = true;
                stopLock.unlock();
        }
        
        private boolean start = false;
        private boolean stop = false;
        private int moveCol;
        
        private ReentrantLock startLock = new ReentrantLock();
        private ReentrantLock stopLock = new ReentrantLock();
        
        private Thread thread = new Thread() {
                @Override
                public void run() {
                        boolean st, sp;
                        while (true) {
                                startLock.lock();
                                st = start;
                                if (start) start = false;
                                startLock.unlock();
                                
                                if (st) {
                                        requestMove();
                                }
                                
                                stopLock.lock();
                                sp = stop;
                                stopLock.unlock();
                                if (sp) break;
                                
                                try {
                                        sleep(100);
                                } catch (InterruptedException e) {
                                        e.printStackTrace();
                                }
                        }
                }
        };
}
