package com.pulp.campaigntracker.http;

import java.util.List;

import org.apache.http.NameValuePair;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import com.pulp.campaigntracker.utils.TLog;

public class PulpHTTPTask extends AsyncTask<List<NameValuePair>, Void, Boolean> implements ActivityCallableTask
{
	boolean finished;

	private FinishableEvent finishableEvent;

	private PulpHttpRequest[] requests;

	private int errorStringId;


	public PulpHTTPTask(FinishableEvent activity, int errorStringId)
	{
		this(activity, errorStringId, true);
	}

	public PulpHTTPTask(FinishableEvent activity, int errorStringId, boolean addToken)
	{
		this.finishableEvent = activity;
		this.errorStringId = errorStringId;
		
	}

	@Override
	protected void onPostExecute(Boolean result)
	{
		finished = true;
		if (finishableEvent != null)
		{
			finishableEvent.onFinish(result.booleanValue());
		}
		if (result.booleanValue())
		{
			for (PulpHttpRequest request : requests)
			{
				request.onSuccess();
			}
		}
		else
		{

			for (PulpHttpRequest request : requests)
			{
				request.onFailure();
			}

			int duration = Toast.LENGTH_LONG;
			if (finishableEvent != null)
			{
				Toast toast = Toast.makeText((Activity) finishableEvent, errorStringId, duration);
				toast.show();
			}
		}
	}

	@Override
	protected Boolean doInBackground(List<NameValuePair>... params)
	{
		this.requests = requests;
		try
		{
			for (PulpHttpRequest pulpHttpRequest : requests)
			{
				TLog.d("HikeHTTPTask", "About to perform request:" + pulpHttpRequest.getPath());
				//AccountUtils.performRequest(hikeHttpRequest, addToken);
				TLog.d("HikeHTTPTask", "Finished performing request:" + pulpHttpRequest.getPath());
			}
		}
		catch (Exception e)
		{
			
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	@Override
	public void setActivity(Activity activity)
	{
		this.finishableEvent = (FinishableEvent) activity;
	}

	@Override
	public boolean isFinished()
	{
		return finished;
	}

	@SuppressWarnings("unchecked")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void executeOnHoneyComb()
	{
		executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	
	

}
