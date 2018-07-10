package com.ebdz.ble.di.util

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
        private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator: Provider<ViewModel>? = creators[modelClass]
        if (creator == null) {
            for ((clazz, classProvider) in creators) {
                if (modelClass.isAssignableFrom(clazz)) {
                    creator = classProvider
                    break
                }
            }
        }

        if (creator == null) {
            throw IllegalArgumentException("unknown class model: $modelClass")
        }

        @Suppress("UNCHECKED_CAST") // checked manually above
        return creator.get() as T
    }
}
