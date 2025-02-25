/*
 * Copyright (C) 2016 The Android Open Source Project
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

package androidx.test.uiautomator;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.app.Instrumentation;
import android.app.UiAutomation;
import android.os.Build;

import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UiDeviceTests {

    private Instrumentation mMockInstrumentation;
    private UiDevice mDevice;
    private int mDefaultFlags;

    @Before
    public void setUp() {
        mMockInstrumentation = mock(Instrumentation.class);
        UiAutomation automation = InstrumentationRegistry.getInstrumentation().getUiAutomation();
        doReturn(automation).when(mMockInstrumentation).getUiAutomation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            doReturn(automation).when(mMockInstrumentation).getUiAutomation(anyInt());
        }
        mDevice = new UiDevice(mMockInstrumentation);
        mDefaultFlags = Configurator.getInstance().getUiAutomationFlags();
    }

    @After
    public void tearDown() {
        Configurator.getInstance().setUiAutomationFlags(mDefaultFlags);
    }

    @Test
    @SdkSuppress(maxSdkVersion = Build.VERSION_CODES.M)
    public void testGetUiAutomation_withoutFlags() {
        mDevice.getUiAutomation();
        // Verify that the UiAutomation instance was obtained without flags (prior to N).
        verify(mMockInstrumentation, atLeastOnce()).getUiAutomation();
    }

    @Test
    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.N)
    public void testGetUiAutomation_withDefaultFlags() {
        mDevice.getUiAutomation();
        // Verify that the UiAutomation instance was obtained with default flags (N+).
        verify(mMockInstrumentation, atLeastOnce()).getUiAutomation(eq(mDefaultFlags));
    }

    @Test
    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.N)
    public void testGetUiAutomation_withCustomFlags() {
        int customFlags = 5;
        Configurator.getInstance().setUiAutomationFlags(customFlags);
        mDevice.getUiAutomation();
        // Verify that the UiAutomation instance was obtained with custom flags (N+).
        verify(mMockInstrumentation, atLeastOnce()).getUiAutomation(eq(customFlags));
    }
}
