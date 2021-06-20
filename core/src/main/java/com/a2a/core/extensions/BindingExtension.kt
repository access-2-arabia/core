package com.a2a.core.extensions

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import com.a2a.core.utility.FragmentViewBindingDelegate

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, { it?.let { t -> action(t) } })
}

fun <T : ViewBinding> Fragment.viewBinding(
    viewBindingFactory: (View) -> T,
    cleanUp: ((T?) -> Unit)? = null,
) = FragmentViewBindingDelegate(this, viewBindingFactory, cleanUp)
fun <T : ViewBinding> DialogFragment.viewBinding(
    viewBindingFactory: (View) -> T,
    cleanUp: ((T?) -> Unit)? = null,
) = FragmentViewBindingDelegate(this, viewBindingFactory, cleanUp)

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) { bindingInflater.invoke(layoutInflater) }



