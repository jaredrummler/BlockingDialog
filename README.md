## Blocking Dialog

Show a dialog from a background thread and wait for a result. Discussed on StackOverflow [here](http://stackoverflow.com/q/2028697/1048340) and [here](http://stackoverflow.com/q/4381296/1048340).

## Usage

1. Create a dialog that extends `BlockingDialogFragment`
2. Show the dialog from a worker thread using `BlockingDialogManager.showAndWait(activity, dialog)`

## License

    Copyright 2017 JRummy Apps Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.