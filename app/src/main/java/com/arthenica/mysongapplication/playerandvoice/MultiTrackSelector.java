package com.arthenica.mysongapplication.playerandvoice;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.RendererConfiguration;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.FixedTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.util.MimeTypes;

import java.util.ArrayDeque;
import java.util.Queue;

public class MultiTrackSelector extends TrackSelector {

	@Override
	public TrackSelectorResult selectTracks(RendererCapabilities[] rendererCapabilities, TrackGroupArray trackGroups, MediaSource.MediaPeriodId periodId, Timeline timeline) throws ExoPlaybackException {
		Queue<Integer> audioRenderers = new ArrayDeque<>();
		Queue<Integer> videoRenderers = new ArrayDeque<>();
		RendererConfiguration[] configs = new RendererConfiguration[rendererCapabilities.length];
		FixedTrackSelection[] selections = new FixedTrackSelection[rendererCapabilities.length];
		for (int i = 0; i < rendererCapabilities.length; i++) {
			if(rendererCapabilities[i].getTrackType() == C.TRACK_TYPE_AUDIO) {
				audioRenderers.add(i);
				configs[i] = RendererConfiguration.DEFAULT;
			} else if (rendererCapabilities[i].getTrackType() == C.TRACK_TYPE_VIDEO) {
				videoRenderers.add(i);
				configs[i] = RendererConfiguration.DEFAULT;
			}
		}
		for (int i = 0; i < trackGroups.length; i++) {
			if (MimeTypes.isAudio(trackGroups.get(i).getFormat(0).sampleMimeType)) {
				Integer index = audioRenderers.poll();
				if (index != null) {
					selections[index] = new FixedTrackSelection(trackGroups.get(i), 0);
				}
			} else if (MimeTypes.isVideo(trackGroups.get(i).getFormat(0).sampleMimeType)) {
				Integer index = videoRenderers.poll();
				if (index != null) {
					selections[index] = new FixedTrackSelection(trackGroups.get(i), 0);
				}
			}
		}
		return new TrackSelectorResult(configs, selections, new Object());

	}

	@Override
	public void onSelectionActivated(@Nullable Object info) {

	}

}
