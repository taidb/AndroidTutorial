package com.example.androidtutorial.activity.util

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.example.androidtutorial.R
object SpannableHelper {

    fun setupTermsAndPrivacyText(
        context: Context,
        textView: AppCompatTextView,
        onTermsClick: (() -> Unit)? = null,
        onPrivacyClick: (() -> Unit)? = null
    ) {
        try {
            val fullText = context.getString(R.string.by_continuing_you_agree_to_our_terms_and_privacy_policies)

            println("FULL TEXT: $fullText")

            val spannable = SpannableString(fullText)

            val terms = "Terms"
            val privacy = "Privacy policies"

            val termsStart = fullText.indexOf(terms)
            val termsEnd = termsStart + terms.length
            val privacyStart = fullText.indexOf(privacy)
            val privacyEnd = privacyStart + privacy.length


            if (termsStart != -1) {
                val termsSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        println("TERMS CLICKED!")
                        onTermsClick?.invoke() ?: Toast.makeText(context, "Terms clicked", Toast.LENGTH_SHORT).show()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = true
                        ds.color = Color.WHITE
                    }
                }
                spannable.setSpan(termsSpan, termsStart, termsEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            }

            if (privacyStart != -1) {
                val privacySpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        println("PRIVACY CLICKED!")
                        onPrivacyClick?.invoke() ?: Toast.makeText(context, "Privacy clicked", Toast.LENGTH_SHORT).show()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = true
                        ds.color = Color.WHITE
                    }
                }
                spannable.setSpan(privacySpan, privacyStart, privacyEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            }

            textView.text = spannable
            textView.movementMethod = LinkMovementMethod.getInstance()
            textView.highlightColor = Color.TRANSPARENT

            textView.isClickable = true
            textView.isFocusable = true


        } catch (e: Exception) {
            println("ERROR in SpannableHelper: ${e.message}")
            e.printStackTrace()
        }
    }
}