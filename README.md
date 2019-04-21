# Progress Circle

This is a progress bar implemented as circle. The inner area is editable and presented as RelativeLayout, so you can put anything inside.

![source (kotlin](https://github.com/p2lem8dev/progress-circle/blob/master/ProgressCircle.kt)

![sample (xml)](https://github.com/p2lem8dev/progress-circle/blob/master/sample.xml)


![](https://github.com/p2lem8dev/progress-circle/blob/master/phone.jpg)
![](https://github.com/p2lem8dev/progress-circle/blob/master/phone_2.jpg)

#### Attributes

<table>
    <tr>
        <td>app:progress_bar_size</td>
        <td>Float</td>
    </tr><tr>
        <td>app:radius</td>
        <td>Float</td>
    </tr><tr>
        <td>android:progress</td>
        <td>Integer</td>
    </tr><tr>
        <td>android:background</td>
        <td>Color</td>
    </tr><tr>
        <td>app:primary_color</td>
        <td>Color</td>
    </tr><tr>
        <td>app:secondary_color</td>
        <td>Color</td>
    </tr><tr>
        <td>app:outline</td>
        <td>Boolean</td>
    </tr>
    <tr>
        <td>app:outline_color</td>
        <td>Color</td>
    </tr>
</table>

#### Events

<code>interface OnProgressChangeListener</code>
<br>
<code>onProgressChange(Float, Int)</code>

#### Properties

<table>
    <tr>
        <td>progress</td>
        <td>Float</td>
    </tr>
    <tr>
        <td>progressBarWidth</td>
        <td>Float</td>
    </tr>
    <tr>
        <td>progressChangeListener</td>
        <td>OnProgressChangeListener?</td>
    </tr>
    <tr>
        <td>radius</td>
        <td>Float</td>
    </tr>
    <tr>
        <td>isRenderOutlineCircle</td>
        <td>Boolean</td>
    </tr>
    <tr>
        <td>outlineColor</td>
        <td>Int</td>
    </tr>
    <tr>
        <td>primaryColor</td>
        <td>Int</td>
    </tr>
    <tr>
        <td>secondaryColor</td>
        <td>Int</td>
    </tr>
    <tr>
        <td>outlineColor</td>
        <td>Int</td>
    </tr>
    <tr>
        <td>duration</td>
        <td>Int</td>
    </tr><tr>
        <td>time</td>
        <td>Int</td>
    </tr>
</table>

#### Methods

<table>
    <tr>
        <td>reset(void)</td>
    </tr>
    <tr>
        <td>setCustomShader(Shader)</td>
    </tr>
    <tr>
        <td>translate(int)</td>
    </tr>
</table>

#### Example

<pre>
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val progressCircle: ProgressCircle = findViewById(R.id.progress_circular)
        progressCircle.progressChangeListener = this
        
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource("audio.mp3")
        progressCircle.duration = mediaPlayer.duration
    }

override fun onProgressChange(progress: Float, time: Int) {
    mediaPlayer.seekTo(time * 1000)
}
</pre>

#### Gestures

<table>
    <tr>
        <td>Move (forward/backward)</td>
        <td>Move finger from (let-right) (right-left). The gesture acceleration effect move speed. So short quick swipe will move the progress more then long slow</td>
    </tr>
</table>
