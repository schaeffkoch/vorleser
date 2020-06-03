package com.example.vorleser

import android.app.*
import android.content.Context
import android.content.res.Configuration
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.nio.charset.Charset
import java.util.*
import android.os.Bundle as Bundle1
import android.os.*
import android.widget.*
import android.content.Intent as Intent
import android.net.Uri
import android.widget.SeekBar.OnSeekBarChangeListener


//import kotlin.coroutines.*
//import kotlin.coroutines.CoroutineContext

private var do_init: Boolean = true
private var otext : String = ""
private var idx : Int=0
const val num_of_lines=10000
const val fast_step=10
private var txt_list = listOf<String>("")
private var do_stop:Boolean=true
private var speaker_language = Locale.getDefault()

/*class SpeakerService : Service(),TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    //private var do_stop: Boolean = true
    private var mbinder: IBinder? = null

    companion object {
        var is_running : Boolean = false
    }
    override fun onCreate() {
        // The service is being created
        tts = TextToSpeech(this, this)
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                //Call your function here
                this@SpeakerService.speakOut()
                handler.postDelayed(this, 200)//1 sec delay
            }
        }, 0)
        is_running=true

    }


    override fun onBind(intent: Intent): IBinder? {
        // A client is binding to the service with bindService()
        return mbinder
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.getDefault())

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            } else {
                //bt_play!!.isEnabled = true
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }


    }

//    override fun onStop() {
//        //super.onStop()
//        if (tts!=null) {
//            tts!!.shutdown()
//        }
//    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun speakOut() {
        //do_stop=false
        //bt_play!!.isEnabled=false
        //while (do_stop==false)  {
        if (do_stop==false) {
            //do_stop=bt_stop!!.isPressed()

            var text = txt_list[idx]
            //val result =tts!!.setLanguage(this.get_language())
/*        if (idx < fluch.size-1) {
            idx++ }
        else {
            idx=0}*/

            if (tts!!.isSpeaking ==false) {
                //MainActivity.update_progress()
                tts!!.setLanguage(speaker_language)
                tts!!.speak(text, TextToSpeech.QUEUE_ADD, null, "")
                if (idx < txt_list.size-1) {
                    //idx += 1
                } else {
                    idx = 0
                }
                //seek_bar.setProgress(idx, true)
                //progress_vw=


            }
        } else {
            tts!!.stop()
        }
        //textView.text=text

    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        is_running=false
        //stopSelf()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()
        return super.onStartCommand(intent, flags, startId)
    }

    public fun set_language(lang : Locale) {
        tts!!.language = lang
    }
}*/

class MainActivity : AppCompatActivity(),TextToSpeech.OnInitListener {



    //private var idx: Int = 0
    //private var do_stop:Boolean=true
    private var fluch=arrayOf("Scheiss", "Herrgott nochmal", "Arschloch", "Fuck off", "Halleluja", "Holla die Waldfee", "Zee Fix", "Gruzi Fix", "Gruzi Türken", "Aff", "Am Depp sei Brotzeitbeitl", "Antn", "Asphaltschwoibn","Auf da Brennsuppn dahergschwumma", "Bagage", "Bamhackleter", "Bazi", "Miststück", "Mistkerl", "Saukerl", "Schwein", "Dumme Kuh", "Ochs", "Pferdegesicht", "Du kleine Kröte", "Saubeutel")
    //private val scope = MainActivity()
    //private val speaker_serv = SpeakerService()
    //private val Speaker = SpeakerService()
    //var intent_speaker : Intent//(MainActivity, SpeakerService::class.java)
    lateinit var wl: PowerManager.WakeLock
    private var tts: TextToSpeech? = null
    private var file_name : Uri? = null
    private val replace_chr = listOf("\n", "\r", "d.h.", "e.g.")
    private val new_chr = listOf(" ", " ", "dh", "eg")
    private val pmenu_title = listOf("Pitch", "Speed")
    private val bg_handler = Handler()




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle1?) {
        //val intent_speaker = Intent(this, SpeakerService::class.java)
//        if (intent_speaker.is_running==false) {
//
//        }



//        val pendingIntent: PendingIntent =
//            Intent(this, SpeakerService::class.java).let { notificationIntent ->
//                PendingIntent.getActivity(this, 0, notificationIntent, 0)
//            }
//
//        val notification: Notification = Notification.Builder(this)
//            .setContentText("Hallo")
//            .setContentIntent(pendingIntent)
//            .build()
//
//        startForeground( notification)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val intent_speaker=Intent(this,SpeakerService::class.java)
//        startService(intent_speaker)

        //this.window.addFlags(FLAG_KEEP_SCREEN_ON)
//        val pm =getSystemService(Context.POWER_SERVICE)
//        wl=pm.new newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "my tag")
//        wl.acquire()
        wl= (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My tag").apply {
                    acquire()
                }
            }

        if (otext=="") {
            otext=gen_txt()
        }
        txt_list = otext.split(".", "\n")
        tv1.text = number_text(txt_list)
        //seek_bar.max=txt_list.size-1

        bt_play!!.isEnabled = true
        bt_play!!.setOnClickListener { //do_stop=false
            bt_play!!.isEnabled=false
            do_stop=false
            //this.startLockTask()
             }
        bt_stop!!.setOnClickListener {
            do_stop = true
            bt_play!!.isEnabled = true
            ///this.tts!!.stop()
            if (idx > 0){
                idx-=1
                progress_vw.text=idx.toString()
            }
            //this.stopLockTask()
        }
        bt_fwd!!.setOnClickListener{
            if (idx < txt_list.size-1) {
                idx+=1
                progress_vw.text=idx.toString()
            }
        }
        bt_back!!.setOnClickListener{
            if (idx > 0) {
                idx-=1
                progress_vw.text=idx.toString()
            }
        }
        bt_file!!.setOnClickListener {
            do_stop=true
            bt_play.isEnabled=true
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_OPEN_DOCUMENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
            //load_file()


        }
        bt_set!!.setOnClickListener{
            val pmenu = PopupMenu(this, bt_set)
            //Inflate our menu Layout.
            pmenu.menuInflater.inflate(R.menu.popup_menu, pmenu.menu)


            //Set Click Listener on Popup Menu Item
            pmenu.setOnMenuItemClickListener { myItem ->

                //Getting Id of selected Item
                val item = myItem!!.itemId

                when (item) {
                    R.id.lang -> {
                        show_lang_dialog()
                    }

                    R.id.pitch -> {
                        //background.setBackgroundColor(Color.B
                        this.show_set_dialog(1, 1, 40, pmenu)
                    }
                    R.id.speed -> {
                        this.show_set_dialog(2, 1, 30, pmenu)
                    }


                }

                true
            }
            pmenu.show()
            //show_lang_dialog()
        }

        bt_seek!!.setOnClickListener{
            show_seek_dialog()
        }

        // The service is being created


        tts = TextToSpeech(this, this)

        bg_handler.postDelayed({
                //Call your function here
            this.bg_speak_task()
            }, 0)

        //Speaker.startService(this.intent)
        do_init = false

    }

    private fun bg_speak_task() {
        this@MainActivity.speakOut()
        bg_handler.postDelayed({this@MainActivity.bg_speak_task()}, 300)//1 sec delay

    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(speaker_language)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            } else {
                //bt_play!!.isEnabled = true
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }


    }

    private fun show_set_dialog(item_idx: Int, min : Int, max : Int, pmenu:PopupMenu){
        lateinit var dialog : AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle(pmenu.menu.getItem(item_idx).title)
        val sbar =SeekBar(this)
        sbar.min=min
        sbar.max=max
        sbar.setProgress(10)
        sbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(sbar:SeekBar, i:Int, b:Boolean) {
                val pv : Float = i/10f
                Toast.makeText(this@MainActivity, pv.toString(), 1000).show()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
                //Toast.makeText(applicationContext,"start tracking",Toast.LENGTH_SHORT).show()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
                //Toast.makeText(applicationContext, "stop tracking", Toast.LENGTH_SHORT).show()
            }
            })
        builder.setView(sbar)
        builder.setPositiveButton("ok", {_, which ->
            kotlin.run{
                val sidx: Float=sbar.progress/10.0f
                when (item_idx) {
                    1 -> tts!!.setPitch(sidx)
                    2 -> tts!!.setSpeechRate(sidx)
                }
                //tts!!.setPitch(sidx)


                dialog.dismiss()
            }
        })
        builder.setNegativeButton("cancle", {_,which-> dialog.dismiss()})
        dialog=builder.create()
        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun speakOut() {
        //do_stop=false
        //bt_play!!.isEnabled=false
        //while (do_stop==false)  {
        if (do_stop==false) {
            //do_stop=bt_stop!!.isPressed()

            var text = txt_list[idx]
            //val result =tts!!.setLanguage(this.get_language())
/*        if (idx < fluch.size-1) {
            idx++ }
        else {
            idx=0}*/

            if (tts!!.isSpeaking ==false) {
                //MainActivity.update_progress()

                tts!!.speak(text, TextToSpeech.QUEUE_ADD, null, "")
                if (idx < txt_list.size-1) {
                    idx += 1
                } else {
                    idx = 0
                }
                //seek_bar.setProgress(idx, true)
                this.update_progress()


            }
        } else {
            tts!!.stop()
        }
        //textView.text=text

    }


    public fun update_progress() {
        this@MainActivity.runOnUiThread(java.lang.Runnable {  
        progress_vw.text=idx.toString()})
    }

    fun show_lang_dialog() {
        lateinit var dialog : AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Language")
        val items = arrayOf("US", "French", "German")
        val checked_item=0
        builder.setSingleChoiceItems(items, checked_item,  {_,which->
             kotlin.run{
                var lang : Locale = Locale.getDefault()
                when (which) {
                    0 -> lang=Locale.US
                    1->lang=Locale.FRANCE
                    2 ->  lang=Locale.GERMANY

                }
                //tts!!.language = lang
                speaker_language=lang
                tts!!.setLanguage(speaker_language)
                dialog.dismiss()
            }
        })
        dialog=builder.create()
        dialog.show()
    }

    fun show_seek_dialog(){

        lateinit var dialog : AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seek")
        val seek_picker = NumberPicker(this)
        //val d_layout=LinearLayout(this)

        seek_picker.minValue=0
        seek_picker.maxValue=txt_list.size/ fast_step

        //eek_picker.value=idx
        builder.setView(seek_picker)
        //builder.setView(seek_picker1)
        //builder.setView(seek_picker2)
        builder.setPositiveButton("ok", {_, which ->
            kotlin.run{
                val sidx=seek_picker.getValue()* fast_step
                if (sidx< txt_list.size+1){
                    idx = sidx
                    progress_vw.text=idx.toString()
                }

                dialog.dismiss()
            }
        })
        builder.setNegativeButton("cancle", {_,which-> dialog.dismiss()})
        dialog=builder.create()
        dialog.show()
    }

    fun number_text(txt: List<String>):String {
        val sep1= "\n"
        var rtxt : String =""
        var n : Int
        for (n in 0..(txt.size-1)) {
            if (txt[n].isBlank()==false) {
                rtxt = rtxt + "(" + n.toString() + ") " + txt[n]+ "\n"
            }
        }

        return rtxt
    }



    /*fun onStartCommand(intend: Intent, flags : Int, startid : Int) :Int {
        //startForeground(1)
        return START_STICKY
   }*/

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data//The uri with the location of the file

            file_name = selectedFile
            load_file()
        }
    }

    fun load_file() {

        val ll = LinearLayout(this)
        ll.orientation=LinearLayout.VERTICAL
        val progress_bar=ProgressBar(this)
        val label_progress=TextView(this)
        progress_bar.max=100
        label_progress.text="File loading.."
        ll.addView(progress_bar)
        ll.addView(label_progress)
        val progress_dialog = AlertDialog.Builder(this).create()
        progress_dialog.setView(ll)
        //val progress_frag = DialogFragment(this)

        progress_dialog.show()

        try {
                //val st_split =selectedFile!!.lastPathSegment.toString().split(":")
                //val path2file =  Environment.getExternalStorageDirectory().toString()+"/"+st_split[1]
                val filedes = this.contentResolver.openInputStream(file_name!!)
                otext=filedes!!.bufferedReader(Charset.defaultCharset()).readText()
//                otext=""
//                filedes!!.bufferedReader(Charset.defaultCharset()).forEachLine { otext=otext+it
//                    progress_load.incrementProgressBy(1)
//                }
                progress_bar.setProgress(10, true)
                filedes.close()
                idx=0

                //otext=path2file.toString()
                //otext = applicationContext.assets.open(selectedFile).bufferedReader(Charsets.UTF_8).toString()
                //otext=File(selectedFile).readText(Charsets.UTF_8)
            } finally {
                //otext="oherrorerrorerror"
                Toast.makeText(this, file_name.toString(), 1000).show()
            }
            //val readData = selectedFile.readText()
            //val readData = selectedFile.

            //otext=readData
            //otext=otext.replace("\n"," ")
            //progress_load.incrementProgressBy(10)
            //otext=otext.replace("\r", " ")
            var n : Int = 0
            for (n in replace_chr.indices) {
                otext=otext.replace(replace_chr[n], new_chr[n])
            }
            txt_list = otext.split(".", "\n", ":", ";")

            if (txt_list.size > num_of_lines) {
                txt_list = txt_list.subList(0, num_of_lines)
            }
            this.tv1?.text = number_text(txt_list)

            //seek_bar.max=txt_list.size-1
            idx=0
            progress_vw.text=idx.toString()
            //seek_bar.setProgress(idx, true)
            progress_dialog.dismiss()


    }

    override fun onConfigurationChanged(new_config : Configuration){
        do_init=false
        super.onConfigurationChanged(new_config)
        Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
    }

    fun gen_txt(): String {
        var s: String = "0"
        var n: Int
        for (x in 0..1000) {
            s = s + fluch[x%fluch.size]+x.toString() + "\n"

        }
        return s
    }

    private fun stop_speak() {
        //do_stop=true
        //tts!!.stop()
    }

    //@RequiresApi(Build.VERSION_CODES.LOLLIPOP)


    public override fun onDestroy() {
        // Shutdown TTS
        //this.stopService(intent_speaker)
        //this.stopService(intent_speaker)
        wl.release()
        super.onDestroy()
    }
}


