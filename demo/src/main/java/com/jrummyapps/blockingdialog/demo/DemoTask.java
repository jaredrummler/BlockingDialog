/*
 * Copyright (C) 2017 JRummy Apps Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jrummyapps.blockingdialog.demo;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;
import com.jrummyapps.blockingdialog.BlockingDialogManager;
import com.jrummyapps.blockingdialog.ResultListener;
import java.lang.ref.WeakReference;

public class DemoTask extends AsyncTask<Void, Void, String> implements ResultListener<String> {

  private WeakReference<Activity> weakActivity;
  private String result;
  private boolean cancelled;

  public DemoTask(Activity activity) {
    weakActivity = new WeakReference<>(activity);
  }

  @Override protected String doInBackground(Void... params) {

    doSomeWork(500);

    Activity activity = weakActivity.get();
    if (activity != null && !activity.isFinishing()) {
      BlockingDialogManager.getInstance().showAndWait(activity, new DemoDialog(), this);
    }

    if (!cancelled) {
      doSomeWork(1500);
    }

    return result;
  }

  @Override protected void onPostExecute(String message) {
    Activity activity = weakActivity.get();
    if (activity != null && !activity.isFinishing() && !cancelled) {
      Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
  }

  @Override public void onResult(String result, boolean cancelled) {
    this.cancelled = cancelled;
    this.result = result;
  }

  private void doSomeWork(int millis) {
    try {
      Thread.sleep(millis); // simulate work
    } catch (InterruptedException ignored) {
    }
  }

}
