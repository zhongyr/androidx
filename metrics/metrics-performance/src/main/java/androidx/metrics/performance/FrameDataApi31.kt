/*
 * Copyright 2022 The Android Open Source Project
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

package androidx.metrics.performance

/**
 * This class stores duration data for a single frame.
 *
 * This subclass of [FrameData] adds an additional value for [frameDurationCpuNanos]
 *
 * @property frameStartNanos The time at which this frame began (in nanoseconds)
 * @property frameDurationUiNanos The time spent in the UI portion of this frame (in nanoseconds).
 * This is essentially the time spent on the UI thread to draw this frame, but does
 * not include any time spent on the RenderThread.
 * @property frameDurationCpuNanos The time spent in the non-GPU portions of this frame (in
 * nanoseconds).  This includes the time spent on the UI thread [frameDurationUiNanos] plus time
 * spent on the RenderThread.
 * @property frameOverrunNanos The amount of time past the frame deadline that this frame took to
 * complete. A positive value indicates some jank, a negative value indicates that the frame was
 * complete within the given deadline.
 * @property isJank Whether this frame was determined to be janky, meaning that its
 * duration exceeds the duration determined by the system to indicate jank (@see
 * [JankStats.jankHeuristicMultiplier]).
 * @property states The UI/app state during this frame.
 * This is the information set by the app, or by other library code, that can be analyzed
 * later to determine the UI state that was current when jank occurred.
 *
 * @see JankStats.jankHeuristicMultiplier
 * @see PerformanceMetricsState.addState
 */
class FrameDataApi31(
    frameStartNanos: Long,
    frameDurationUiNanos: Long,
    frameDurationCpuNanos: Long,
    frameOverrunNanos: Long,
    isJank: Boolean,
    states: List<StateInfo>
) : FrameDataApi24(frameStartNanos, frameDurationUiNanos, frameDurationCpuNanos, isJank, states) {

    var frameOverrunNanos = frameOverrunNanos
        private set

    override fun copy(): FrameData {
        return FrameDataApi31(frameStartNanos, frameDurationUiNanos, frameDurationCpuNanos,
            frameOverrunNanos, isJank, ArrayList(states))
    }

    internal fun update(
        frameStartNanos: Long,
        frameDurationUiNanos: Long,
        frameDurationCpuNanos: Long,
        frameOverrunNanos: Long,
        isJank: Boolean
    ) {
        super.update(frameStartNanos, frameDurationUiNanos, frameDurationCpuNanos, isJank)
        this.frameOverrunNanos = frameOverrunNanos
    }

    override fun equals(other: Any?): Boolean {
        return other is FrameDataApi31 &&
            super.equals(other) &&
            (frameOverrunNanos == other.frameOverrunNanos)
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + frameOverrunNanos.hashCode()
    }

    override fun toString(): String {
        return "FrameData(frameStartNanos=$frameStartNanos, " +
            "frameDurationUiNanos=$frameDurationUiNanos, " +
            "frameDurationCpuNanos=$frameDurationCpuNanos, " +
            "frameOverrunNanos=$frameOverrunNanos, " +
            "isJank=$isJank, " +
            "states=$states)"
    }
}