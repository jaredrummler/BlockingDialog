/*
 * Copyright (C) 2017 Jared Rummler
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

package com.jaredrummler.blockingdialog;

/**
 * Callback that is invoked from a {@link BlockingDialogFragment}
 *
 * @param <Result>
 *     The expected result from the dialog
 */
public interface ResultListener<Result> {

  /**
   * Called when a result is set from a {@link BlockingDialogFragment}
   *
   * @param result
   *     The result. Usually the user input from interacting with a dialog.
   * @param cancelled
   *     {@code true} if the dialog was cancelled
   */
  void onResult(Result result, boolean cancelled);
}