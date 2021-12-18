package app.moviebase.androidx.widget.recyclerview.list

import androidx.recyclerview.widget.DiffUtil
import app.moviebase.androidx.widget.recyclerview.adapter.*
import app.moviebase.androidx.widget.recyclerview.diff.ItemDiffCallback
import app.moviebase.androidx.widget.recyclerview.glide.GlideConfig
import app.moviebase.androidx.widget.recyclerview.glide.GlideViewLoader
import kotlin.reflect.KClass

class ListItemAdapterConfig<T : Any> : ItemAdapterConfig<T> {

    override var onClickListener: OnClickListener<T>? = null
    override var onLongClickListener: OnLongClickListener<T>? = null

    var onViewType: OnViewType = DefaultOnViewType()
    var onItemId: OnItemId<T> = OnValueHashCode()
    val viewHolders = mutableMapOf<Int, ViewHolderBuilder<T>>()

    var diffCallback: DiffUtil.ItemCallback<T> = ItemDiffCallback()

    var glideConfig: GlideConfig<T> = GlideConfig()
    var glideLoader: GlideViewLoader<in T>?
        get() = glideConfig.loader
        set(value) {
            glideConfig.loader = value
        }

    fun onViewType(onViewType: OnViewType) {
        this.onViewType = onViewType
    }

    fun onItemId(onItemId: OnItemId<T>) {
        this.onItemId = onItemId
    }

    fun onClick(onClick: (T) -> Unit) {
        onClickListener = OnClickListener { value, _ -> onClick(value) }
    }

    fun onLonClick(onClick: (T) -> Unit) {
        onLongClickListener = OnLongClickListener { value -> onClick(value) }
    }

    fun viewHolder(builder: ViewHolderBuilderFunction<T>) {
        viewHolders[ViewType.VIEW_TYPE_DEFAULT] = ViewHolderBuilder { adapter, parent -> builder(adapter, parent) }
    }

    fun viewHolder(viewType: Int, builder: ViewHolderBuilderFunction<T>) {
        viewHolders[viewType] = ViewHolderBuilder { adapter, parent -> builder(adapter, parent) }
    }

    fun viewHolder(viewType: KClass<out T>, builder: ViewHolderBuilderFunction<T>) {
        onViewType = ClassOnViewType()
        viewHolders[viewType.java.hashCode()] = ViewHolderBuilder { adapter, parent -> builder(adapter, parent) }
    }
}