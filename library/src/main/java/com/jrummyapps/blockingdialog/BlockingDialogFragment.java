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
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * A dialog that can be shown from a background thread and wait for input from the user using
 * {@link BlockingDialogManager#showAndWait(Activity, BlockingDialogFragment, ResultListener)}.
 *
 * @param <T>
 *     The result type
 */
public class BlockingDialogFragment<T> extends DialogFragment {

  private boolean setResult;
  private int requestCode;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState != null) {
      requestCode = savedInstanceState.getInt("requestCode");
      setResult = savedInstanceState.getBoolean("setResult");
    }
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("requestCode", requestCode);
    outState.putBoolean("setResult", setResult);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (!getActivity().isChangingConfigurations()) {
      setResult(null, true);
    }
  }

  /**
   * Set the result.
   *
   * @param result
   *     The result
   * @param cancelled
   *     {@code true} if the dialog was cancelled
   */
  protected void setResult(T result, boolean cancelled) {
    if (!setResult) {
      BlockingDialogManager.getInstance().onResult(requestCode, result, cancelled);
      setResult = true;
    }
  }

  void setRequestCode(int requestCode) {
    this.requestCode = requestCode;
  }

}