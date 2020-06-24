package org.woped.quantana.dashboard.storage;



public class MessageQueue
{
	//private byte[][] msgQueue = null;
	//private StorageData[] msgQueue = null;
	
	//private int qsize = 0;
	//private int head = 0;
	//private int tail = 0;
/*	
	public MessageQueue(int capacity)
	{
		if(capacity <= 0)
			return;
		//msgQueue = new StorageData[capacity];
	}

	
	public synchronized void send(StorageData stoda)
	{
		while(qsize == msgQueue.length)
		{
			System.out.println("queue is full");
			
			try
			{
				wait();
			}
			catch(InterruptedException e)
			{
			}
		}
		//Kopieren der gesendeten Nachricht
		//msgQueue[tail] = stoda;
		qsize++;
		tail++;
		if(tail == msgQueue.length)
			tail = 0;
		notifyAll();
	}
	public synchronized StorageData receive()
	{
		while(qsize == 0)
		{
			try
			{
				wait();
			}
			catch(InterruptedException e)
			{
			}
		}

		StorageData result = msgQueue[head];
		msgQueue[head] = null;
		qsize--;
		head++;
		if(head == msgQueue.length)
			head = 0;
		notifyAll();
		return result;
	}
*/
}