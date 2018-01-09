package com.example.mohammed.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class ColorsFragment extends Fragment {
    // best intialize object in your on create

    /**
     * Handles playback of all the sound files
     */
    private MediaPlayer mMediaPlayer;
    /**
     * Handles audio focus when playing a sound file
     */
    private AudioManager am;

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    public ColorsFragment() {
        // required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // fragment display the word_list layout "R.layout.word_list"
        View rootview = inflater.inflate(R.layout.word_list, container, false);

        // Create and setup the {@link AudioManager} to request audio focus
        /*
        Error :
        You will get an error saying cannot resolve method “getSystemService(String)”
        because the Fragment does not have access to system services, whereas the Activity does .
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        Fix the error by getting the Activity object instance first.
        This is the Activity that encloses the current Fragment,
        which will be the NumbersActivity for the NumbersFragment.
        Then call getSystemService(String) on that Activity object.

        am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
         */
        am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // when define our arraylist as a final we can reference in clicklistener
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Black", "Aswad", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("Brown", "Bonny", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("Dusty Yellow", "Asfar", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        words.add(new Word("Grey", "Romady", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("Green", "Akhdar", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("Mustard Yellow", "Asfar", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));
        words.add(new Word("Red", "Ahmar", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("White", "Abyad", R.drawable.color_white, R.raw.color_white));


        /* view recycler method */

        // Create an {@link ArrayAdapter},
        // whose data source is a list of Strings or objects.
        // The adapter knows how to create layouts for each item in the list, using the
        // simple_list_item_1.xml layout resource defined in the Android framework.
        // This list item layout contains a single {@link TextView}, which the adapter will set to
        // display a single word.
        // and we modify array adapter be create out array adapter with 2 Text Views

        /*
        Error :
        There’s a problem with the arguments passed into the WordAdapter constructor
        because the first parameter “this” refers to this class (which is the NumbersFragment),
        and a Fragment is not a valid Context.
        However, the code used to work when “this” referred the NumbersActivity because an Activity is a valid Context.
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_numbers);

        Fix the error by passing in a reference to the Activity that encloses this Fragment as the context.
        WordAdapter adapter = new WordAdapter(getActivity(), words, R.color.category_numbers);
         */
        WordAdapter adapter =
                new WordAdapter(getActivity(), words, R.color.category_numbers);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_li st.xml file.

        // search through the view hierarchy for the list view was defined in the XML layout.
        /*
        Error :
        You will get get an error saying cannot resolve method “findViewById(int)”
        because the Fragment does not have a findViewById method, whereas the Activity did have that method.
        ListView listView = (ListView) findViewById(R.id.list);

        Fix the error by calling findViewById(int) on the rootView object,
        which should contain children views such as the ListView.
        ListView listView = (ListView) rootView.findViewById(R.id.list);
         */
        ListView listView = (ListView) rootview.findViewById(R.id.list);

        // Make the {@link ListView} use the {@link ArrayAdapter} we created above, so that the
        // {@link ListView} will display list items for each word in the list of words.
        // Do this by calling the setAdapter method on the {@link ListView} object and pass in
        // 1 argument, which is the {@link ArrayAdapter} with the variable name itemsAdapter.

        // associating the array adapter with list view
        listView.setAdapter(adapter);



        /* new AdapterView.OnItemClickListener() -> anonymous class and its methods can only reference local vars
        if they declared As a Final
        Or it can reference global in the same class
        */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // if we didn`t declare arraylist final we can`t reference


                // position attribute can get corresponding word object from the arraylist of words
                // check the words array list to get the word that was clicked using position
                // words.get(position) will return a single word var
                // position lead us to the word object within arraylist  and from word object we can retrieve audio res ID
                // and which word object click
                Word word = words.get(position);

                // to sure that media player not associated to any file
                // Release the media player if the currently exists because we are about to play a different file
                // release the media player res before the media player is initialized  to play another audio file
                // ex: if user click on different songs and app has not enough time to finish the prev song and play new
                // so the app should stop the old and play a new file
                releaseMediaPlayer();

                // Request audio focus so in order to play the audio file. The app needs to play a
                // short audio file, so we will request audio focus with a short amount of time
                // with AUDIOFOCUS_GAIN_TRANSIENT.
                // Request audio focus for playback
                int result = am.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Now we have Audio Focus
                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    /*
                    Error #4: When creating a MediaPlayer object, we need to pass in a context. Again,
                     “this” refers to the NumbersFragment (and not the NumbersActivity),
                      and the Fragment is not a valid Context.
                      mMediaPlayer = MediaPlayer.create(NumbersActivity.this, word.getAudioResourceId());

                    Fix the error by passing in the activity for the first input parameter.
                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getAudioResourceId());
                     */
                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getmAudioResourceId());

                    mMediaPlayer.start();


                    // Async callbacks
                    // Doing something while waiting another something to finish

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    // a2dar a3mel kol dah fe variable we yeb2a gowa relese mogard a3mel pass l var le setOnComplition
                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                            // release the res after the file has finished playing
                            releaseMediaPlayer();

                            //Toast.makeText(NumbersActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        return rootview;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    /*
                 * Clean Up the media player by releasing its resources
                 */
    private void releaseMediaPlayer() {
        // if the media player is not null, then it may be currently palying audio file
        if (mMediaPlayer != null) {
            // Regardless of the current state of the  media player, release its resources
            // because we no longer need it
            mMediaPlayer.release();

            // Set the media player back to null. For our code we decided that.
            // Setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment
            mMediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            am.abandonAudioFocus(mOnAudioFocusChangeListener);

        }
    }

}
