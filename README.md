# MaterialCheckBox

A simple checkbox view interaction that tweens between tick and a cross.

# Demo
![Material Checkbox Demo](https://raw.githubusercontent.com/ishan1604/MaterialCheckBox/master/demo/materialcheckbox-demo.gif)

# Download

Add this to your Top Level `build.gradle` file
```
allprojects {
    repositories {
        maven {
            url 'https://dl.bintray.com/ishan1604/maven/'
        }
    }
}
```
Add this to your app module's `build.gradle` file
```
dependencies {
    compile 'in.ishankhanna.materialcheckboxview:materialcheckboxview:1.0.1'
}
```

# Usage

In your Layout XML add this

```
    <in.ishankhanna.materialcheckboxview.MaterialCheckBox
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/test_checkbox"
        app:mcb_radius="28dp"
        app:mcb_shadowRadius="8dp"
        app:mcb_checkedStateColor="@color/red"
        app:mcb_unCheckedStateColor="@color/green"
        app:mcb_lineColor="@color/white"
        app:mcb_shadowColor="@color/darkerGray"/>

```

| Property                | Description                                              | Format    | Default   |
|-------------------------|----------------------------------------------------------|-----------|-----------|
| mcb_radius              | Radius of the circle.                                    | dimension | 18 dp     |
| mcb_shadowRadius        | Radius of the shadow.                                    | dimension | 0 dp      |
| mcb_checkedStateColor   | Color of the circle with tick.                           | color     | #00E676   |
| mcb_unCheckedStateColor | Color of the circle with cross.                          | color     | #FF373D   |
| mcb_lineColor           | Color of the lines with which tick and cross are formed. | color     | #FFFFFF   |
| mcb_shadowColor         | Color of the shadow.                                     | color     | #80000000 |

Within your activity or fragment you can set a listener like this

```
    MaterialCheckBox materialCheckBox = (MaterialCheckBox) findViewById(R.id.button);
    materialCheckBox.setOnCheckedChangeListener(new MaterialCheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(boolean isChecked) {
            // Have fun here :)
        }
    });
    
```

Note : `onCheckedChanged(boolean isChecked)` Method is called after the animation completes.

# Credits

Inspiration from [https://dribbble.com/shots/1983467-Right-And-Wrong](https://dribbble.com/shots/1983467-Right-And-Wrong)

# Developers

* [Ishan Khanna](https://github.com/ishan1604)
* [Salam Thomas](https://github.com/salamthomas)

# License

```
Copyright 2015 Ishan Khanna, Salam Thomas

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```
