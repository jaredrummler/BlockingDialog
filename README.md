## Blocking Dialog

Show a dialog from a background thread and wait for a result. Discussed on StackOverflow [here](http://stackoverflow.com/q/4381296/1048340) and [here](http://stackoverflow.com/q/2028697/1048340).

<img src="https://img.shields.io/badge/API-11%2B-blue.svg?style=flat" alt="API" /> <a target="_blank" href="LICENSE.txt"><img src="http://img.shields.io/:license-apache-blue.svg" alt="License" /></a> <a target="_blank" href="https://maven-badges.herokuapp.com/maven-central/com.jaredrummler/blocking-dialog"><img src="https://maven-badges.herokuapp.com/maven-central/com.jaredrummler/blocking-dialog/badge.svg" alt="Maven Central" /></a> <img src="https://img.shields.io/badge/methods-20-e91e63.svg" /> <a target="_blank" href="https://twitter.com/jaredrummler"><img src="https://img.shields.io/twitter/follow/jaredrummler.svg?style=social" /></a>

## Usage

**1.** Create a dialog that extends `BlockingDialogFragment`:

```java
public class YesOrNoDialog extends BlockingDialogFragment<String> {

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    return new AlertDialog.Builder(getActivity())
        .setMessage("Do you like Taylor Swift?")
        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            setResult("YES", false);
          }
        })
        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            setResult("NO", false);
          }
        })
        .create();
  }
}
```

**2.** Show the dialog from a worker thread using `BlockingDialogManager#showAndWait`:

```java
public class MyTask extends AsyncTask<Void, Void, Void> {

  WeakReference<Activity> weakActivity;

  ...

  @Override protected Void doInBackground(Void... params) {

    ...

    // Need input from the  user.
    // Show the dialog from the UI thread and wait on this background thread for a result
    Activity activity = weakActivity.get();
    String input = BlockingDialogManager.getInstance().showAndWait(activity, new YesOrNoDialog());

    if (input == null) {
      // user cancelled
    } else if (input.equals("YES")) {
      // do something
    }

    return null;
  }

}
```

See the [demo](demo) project.

## Download

Download [the latest JAR](https://repo1.maven.org/maven2/com/jaredrummler/blocking-dialog/1.0.0/blocking-dialog-1.0.0.jar) or grab via Gradle:

```groovy
compile 'com.jaredrummler:blocking-dialog:1.0.0'
```

## License

    Copyright 2017 Jared Rummler

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
