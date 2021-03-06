/*
 * Copyright (C) 2015 Twitter, Inc.
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
 *
 */

package com.digits.sdk.android;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.InputType;
import android.text.SpannedString;
import android.view.View;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class EmailRequestActivityDelegateTests extends
        DigitsActivityDelegateTests<EmailRequestActivityDelegate> {
    @Override
    public EmailRequestActivityDelegate getDelegate() {
        return spy(new DummyEmailRequestActivityDelegate(scribeService));
    }

    public void testIsValid() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, new ResultReceiver(null));
        bundle.putString(DigitsClient.EXTRA_PHONE, TestConstants.ANY_PHONE);
        assertTrue(delegate.isValid(bundle));
    }

    public void testIsValid_missingResultReceiver() {
        final Bundle bundle = new Bundle();
        bundle.putString(DigitsClient.EXTRA_PHONE, TestConstants.ANY_PHONE);
        assertFalse(delegate.isValid(bundle));
    }

    public void testIsValid_missingPhoneNumber() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, new ResultReceiver(null));
        assertFalse(delegate.isValid(bundle));
    }

    public void testGetLayoutId() {
        assertEquals(R.layout.dgts__activity_confirmation, delegate.getLayoutId());
    }

    @Override
    public void testSetUpTermsText() throws Exception {
        delegate.tosFormatHelper = tosFormatHelper;
        doReturn(new SpannedString("")).when(tosFormatHelper)
                .getFormattedTerms(anyInt());
        super.testSetUpTermsText();
        verify(tosFormatHelper).getFormattedTerms(R.string.dgts__terms_email_request);
        verify(textView).setText(new SpannedString(""));
    }

    @Override
    public void testSetUpSendButton() throws Exception {
        super.testSetUpSendButton();
        verify(button).setStatesText(R.string.dgts__continue, R.string.dgts__sending,
                R.string.dgts__done);
        verify(button).showStart();
    }

    public void testSetUpEditText() throws Exception {
        super.testSetUpEditText_noNextAction();
        verify(editText).setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }

    public void testOnResume() {
        delegate.controller = controller;
        delegate.onResume();
        verify(controller).onResume();
        verify(scribeService).impression();
    }

    public void testSetUpEditPhoneNumberButton() {
        delegate.setUpEditPhoneNumberLink(activity, editPhoneNumberLink, "");
        verify(editPhoneNumberLink).setVisibility(View.GONE);
    }

    public void testSetupResendButton(){
        delegate.setupResendButton(activity, controller, scribeService, resendButton);
        verify(resendButton).setVisibility(View.GONE);
    }

    public void testSetupCallMeButton(){
        final AuthConfig config = new AuthConfig();
        delegate.setupCallMeButton(activity, controller, scribeService, callMeButton, config);
        verify(callMeButton).setVisibility(View.GONE);
    }

    public void testSetupTimerText(){
        final AuthConfig config = new AuthConfig();
        delegate.setupCountDownTimer(controller, timerText, config);
        verify(timerText).setVisibility(View.GONE);
    }

    public class DummyEmailRequestActivityDelegate extends EmailRequestActivityDelegate {

        DummyEmailRequestActivityDelegate(DigitsScribeService scribeService) {
            super(scribeService);
        }
    }
}
