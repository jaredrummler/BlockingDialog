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

package com.jrummyapps.blockingdialog;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.SparseArray;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Show dialogs from a background thread and wait for a result.
 */
@SuppressWarnings("WeakerAccess")
public class BlockingDialogManager {

  /** The tag used for displaying the dialog */
  public static final String DIALOG_TAG = "blocking-dialog";

  public static BlockingDialogManager getInstance() {
    return Singleton.INSTANCE;
  }

  final SparseArray<Request> requests = new SparseArray<>();
  final AtomicInteger requestCode = new AtomicInteger(0);

  BlockingDialogManager() {

  }

  /**
   * Display the provided dialog on the UI thread and wait for a result.
   *
   * @param activity
   *     The current activity
   * @param fragment
   *     The fragment to show
   * @param <T>
   *     The expected result
   */
  @WorkerThread public <T> T showAndWait(
      @NonNull final Activity activity,
      @NonNull final BlockingDialogFragment fragment) {
    return showAndWait(activity, fragment, null);
  }

  /**
   * Display the provided dialog on the UI thread and wait for a result.
   *
   * @param activity
   *     The current activity
   * @param fragment
   *     The dialog fragment to show
   * @param listener
   *     A listener to be notified of a result or if the dialog was cancelled/dismissed.
   * @param <T>
   *     The expected result
   */
  @WorkerThread public <T> T showAndWait(@NonNull final Activity activity,
                                         @NonNull final BlockingDialogFragment fragment,
                                         @Nullable final ResultListener<T> listener) {
    final int key = requestCode.getAndIncrement();
    final Request<T> request = new Request<>(listener);
    requests.put(key, request);

    final Handler handler = new Handler(Looper.getMainLooper());
    handler.post(new Runnable() {
      @Override public void run() {
        fragment.show(activity.getFragmentManager(), DIALOG_TAG);
        fragment.setRequestCode(key);
      }
    });

    if (Looper.myLooper() != Looper.getMainLooper()) {
      try {
        request.semaphore.acquire();
      } catch (InterruptedException ignored) {
      }
    }

    return request.result;
  }

  void onResult(int requestCode, Object result, boolean cancelled) {
    Request request = requests.get(requestCode);
    if (request != null) {
      request.result = result;
      if (request.listener != null) {
        //noinspection unchecked
        request.listener.onResult(result, cancelled);
      }
      request.semaphore.release();
      requests.remove(requestCode);
    }
  }

  private static final class Request<T> {
    final Semaphore semaphore = new Semaphore(0, true);
    final ResultListener listener;
    T result;

    Request(ResultListener listener) {
      this.listener = listener;
    }
  }

  private static final class Singleton {
    static final BlockingDialogManager INSTANCE = new BlockingDialogManager();
  }

}
