package storage

import com.soywiz.korge.service.storage.NativeStorage
import com.soywiz.korge.service.storage.get
import com.soywiz.korge.service.storage.storage
import com.soywiz.korge.view.views
import com.soywiz.korio.lang.InvalidArgumentException

/**
 * A Storage for storing strings.
 *
 * The saved strings are stored in [data]. The Storages are identified by their prefix.
 * Access is provided by [] or manipulation of [data].
 * Use [read] to get and [write] to save the data.
 *
 * Use [invoke] to create a new Storage object.
 */
class Storage private constructor(val storage : NativeStorage,val prefix : String) {

    var data : MutableMap<String,String> = emptyMap<String,String>().toMutableMap()

    operator fun get(key:String):String{
        return data[key] ?: throw InvalidArgumentException()
    }

    operator fun set(key: String,value: String){
        data[key]=value
    }

    init {
        read()
    }

    /**
     * Reads this [Storage] from NativStorage.
     */
    fun read(){
        storage.keys().filter { it.startsWith(prefix) }.forEach {
            data[it.drop(prefix.length)]=storage[it]
        }
    }

    /**
     * Writes this [Storage] to NativStorage.
     */
    fun write(){
        data.forEach {
            storage[prefix+it.key]=it.value
        }
    }

    /**
     * Clears this [Storage].
     *
     * **Handle with care.**
     */
    fun flush(){
        storage.keys().filter { it.startsWith(prefix) }.forEach {
            storage.remove(it)
        }
    }

    companion object{

        /**
         * Creates a new [Storage] object.
         * The [prefix] identifies the Storage.
         */
        suspend operator fun invoke(prefix: String):Storage{
            return Storage(views().storage,prefix)
        }

        /**
         * Clears all existing Storages.
         *
         * **Handle with care.**
         */
        suspend fun flushAll(){
            views().storage.removeAll()
        }

    }

}