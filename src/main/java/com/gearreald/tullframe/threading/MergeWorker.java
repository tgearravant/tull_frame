package com.gearreald.tullframe.threading;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import com.gearreald.tullframe.Row;
import com.gearreald.tullframe.TullFrame;
import com.gearreald.tullframe.interfaces.JoinCondition;

import net.tullco.tullutils.Pair;

public class MergeWorker extends Thread {

	private AtomicBoolean keepRunning;
	private final Queue<Pair<Row, Row>> rowQueue;
	private final TullFrame outputFrame;
	private final JoinCondition condition;
	
	public MergeWorker(Queue<Pair<Row, Row>> rowQueue, TullFrame outputFrame, JoinCondition condition){
		super("merge_worker_" + UUID.randomUUID());
		this.keepRunning=new AtomicBoolean(true);
		this.rowQueue = rowQueue;
		this.outputFrame = outputFrame;
		this.condition = condition;
	}
	@Override
	public void run() {
		while(keepRunning.get() || Thread.interrupted()){
			//The worker will attempt the job from it's queue.
			try{
				Pair<Row, Row> rowsToTest = this.rowQueue.poll();
				if(rowsToTest == null){
					Thread.sleep(10);
					continue;
				}
				
				if(condition.condition(rowsToTest.left(), rowsToTest.right())){
					List<String> output = new ArrayList<String>();
					for(Object o : rowsToTest.left()){
						output.add(o.toString());
					}for(Object o : rowsToTest.right()){
						output.add(o.toString());
					}
					this.outputFrame.addRow(output);
				}else{
					continue;
				}
				
			}catch (InterruptedException e) {
				this.noMore();
			}catch (NullPointerException e) {
				continue;
			}
		}
	}
	public void noMore() {
		this.keepRunning.lazySet(false);
		this.interrupt();
	}
	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}
	@Override
	public boolean equals(Object o){
		if(o==this)
			return true;
		if(!(o instanceof MergeWorker))
			return false;
		MergeWorker w = (MergeWorker) o;
		return w.getName().equals(this.getName());
	}
}
