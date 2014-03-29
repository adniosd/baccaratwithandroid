package com.vivogaming.livecasino.screens.game;

import static com.vivogaming.livecasino.global.Constants.TAG_ERROR;
import static com.vivogaming.livecasino.global.Constants.TAG_VIDEO;
import static com.vivogaming.livecasino.global.Dialogs.showBetOrTipFailDialog;

import java.io.IOException;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.widget.ProgressBar;

import com.vivogaming.livecasino.R;

public abstract class LiveVideoWorker {
	/**
	 * Developer Sam
     * 2014年3月29日
     * replace VideoView with SurfaceView
	 */
    private static SurfaceView vvStreamVideo_SMG;
    private static MediaPlayer mediaPlayer;
    private static ProgressBar pbVideoLoader_SG;
//    private static long HLS_DELAY = 3000;
//    private static long NET_RTT_DELAY = 3000;
//    public static long sTotalOffsetMS = 0;
    private static String videoUrl = "";
    public static final void playVideo(final Activity _activity, final String _liveVideoUrl) {
        Log.d(TAG_ERROR, "_liveVideoUrl = " + _liveVideoUrl);
        final String[] videosArr = _liveVideoUrl.split(",");

        Log.d(TAG_VIDEO, "Video links count: " + videosArr.length);
        for (int i = 0; i < videosArr.length; i++) {
            Log.d(TAG_VIDEO, "[" + i + "]: " + videosArr[0]);
        }

        //final String videoUrl = videosArr[0];
        videoUrl = videosArr[0];
        if (!videoUrl.contains("m3u8")) return;
        final Uri videoUri = Uri.parse(videoUrl);

//      final Uri videoUri = Uri.parse("http://www.playlive21.com/HLS/flashtest.m3u8");      //default
//      final Uri videoUri = Uri.parse("rtsp://hdimages.cdnvideo.ru/rr/HDextremeCDN");    //<------- Work!
//      final Uri videoUri = Uri.parse("rtsp://fms.zulu.mk/zulu/sitel_1");    //<------- Work!
//      final Uri videoUri = Uri.parse("rtsp://hdimages.cdnvideo.ru/rr/HDprocessCDN");    //<------- Work!
//      final Uri videoUri = Uri.parse("rtsp://s1.media-planet.sk/live/reduta");    //<------- Work!

        //loader when video is buffering
        pbVideoLoader_SG    = (ProgressBar) _activity.findViewById(R.id.pbVideoLoader_SG);
        pbVideoLoader_SG.setVisibility(View.VISIBLE);

        /**
    	 * Developer Sam
         * 2014年3月29日
         * replace VideoView with SurfaceView
    	 */
        vvStreamVideo_SMG   = (SurfaceView) _activity.findViewById(R.id.lineVideo).findViewById(R.id.vvStreamVideo_SMG);
        vvStreamVideo_SMG.getHolder().addCallback(callback);
//        /**
//         * 
//         */
//        mediaPlayer = new MediaPlayer();
//		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//		// ���ò��ŵ���ƵԴ
//		try {
//			mediaPlayer.setDataSource(videoUrl);
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalStateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		// ������ʾ��Ƶ��SurfaceHolder
//		mediaPlayer.setDisplay(vvStreamVideo_SMG.getHolder());
//		mediaPlayer.prepareAsync();
//		mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public final boolean onError(final MediaPlayer _mediaPlayer, final int _what, final int _extra) {
//                Log.d(TAG_VIDEO, "video error: what = " + _what + ", extra = " + _extra);
//                showBetOrTipFailDialog(_activity, "video error: what = " + _what + ", extra = " + _extra);  //todo: delete
////                vvStreamVideo_SMG.setVideoURI(videoUri);
//                pbVideoLoader_SG.setVisibility(View.GONE);
//                return true;
//            }
//        });
//
//        final long tSetUri = System.currentTimeMillis();
//        //vvStreamVideo_SMG.setVideoURI(videoUri);
//        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public final void onPrepared(final MediaPlayer _mediaPlayer) {
//                long bufferingDelay = System.currentTimeMillis() - tSetUri;
////                sTotalOffsetMS = HLS_DELAY + NET_RTT_DELAY + bufferingDelay;
////                Log.d(TAG_VIDEO, "onPrepared(): Total offset set to: "+sTotalOffsetMS + "[ms]. Buffer delay is estimated at: " + bufferingDelay);
//                mediaPlayer.start();
//                mediaPlayer.seekTo((int)bufferingDelay);
//                //delete loader when video is prepared
//                pbVideoLoader_SG.setVisibility(View.GONE);
//
//                _mediaPlayer.start();
//            }
//        });
    }

    /**
	 * Developer Sam
     * 2014年3月29日
	 */
    public static final void pauseVideo() {
        if (mediaPlayer == null) return;

        if (mediaPlayer.isPlaying()) mediaPlayer.pause();
    }
    
    private static Callback callback = new Callback() {
		// SurfaceHolder���޸ĵ�ʱ��ص�
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// No need
		}

		/**
		 * Developer Sam
	     * 2014年3月29日
	     * 
		 */
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			/**
	         * 
	         */
	        mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			// ���ò��ŵ���ƵԴ
			try {
				/**
				 * Developer Sam
			     * 2014年3月29日
			     * rset video resource
				 */
				mediaPlayer.setDataSource(videoUrl);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// ������ʾ��Ƶ��SurfaceHolder
			mediaPlayer.setDisplay(vvStreamVideo_SMG.getHolder());
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
	            @Override
	            public final boolean onError(final MediaPlayer _mediaPlayer, final int _what, final int _extra) {
	                Log.d(TAG_VIDEO, "video error: what = " + _what + ", extra = " + _extra);
	                showBetOrTipFailDialog(Game.getGameActivity(), "video error: what = " + _what + ", extra = " + _extra);  //todo: delete
//	                vvStreamVideo_SMG.setVideoURI(videoUri);
	                pbVideoLoader_SG.setVisibility(View.GONE);
	                return true;
	            }
	        });

	        final long tSetUri = System.currentTimeMillis();
	        //vvStreamVideo_SMG.setVideoURI(videoUri);
	        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
	            @Override
	            public final void onPrepared(final MediaPlayer _mediaPlayer) {
	                long bufferingDelay = System.currentTimeMillis() - tSetUri;
//	                sTotalOffsetMS = HLS_DELAY + NET_RTT_DELAY + bufferingDelay;
//	                Log.d(TAG_VIDEO, "onPrepared(): Total offset set to: "+sTotalOffsetMS + "[ms]. Buffer delay is estimated at: " + bufferingDelay);
	                mediaPlayer.start();
	                mediaPlayer.seekTo((int)bufferingDelay);
	                //delete loader when video is prepared
	                pbVideoLoader_SG.setVisibility(View.GONE);

	                _mediaPlayer.start();
	            }
	        });
		}

		/**
		 * Developer Sam
	     * 2014年3月29日
		 */
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			if(mediaPlayer!=null){
				mediaPlayer.setDisplay(holder);
			}
		}

	};
    
}