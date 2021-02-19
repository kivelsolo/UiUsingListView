package com.zeke.home.wanandroid

import android.app.Service
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Html
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.animation.SlideInBottomAnimation
import com.kingz.base.BaseVMFragment
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.common.router.RPath
import com.kingz.module.common.utils.ktx.SDKVersion
import com.kingz.module.home.R
import com.kingz.module.wanandroid.WADConstants
import com.kingz.module.wanandroid.bean.Article
import com.kingz.module.wanandroid.bean.BannerItem
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.youth.banner.Banner
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.ScaleInTransformer
import com.zeke.home.wanandroid.adapter.ArticleAdapter
import com.zeke.home.wanandroid.adapter.HomeBannerAdapter
import com.zeke.home.wanandroid.repository.HomeRepository
import com.zeke.home.wanandroid.viewmodel.HomeViewModel
import com.zeke.kangaroo.utils.ZLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import java.util.*


/**
 * 首页热门推荐(玩android)的Fragemnt
 */
class WanAndroidHomeFragment : BaseVMFragment<HomeRepository, HomeViewModel>() {

    private var banner: Banner<BannerItem, HomeBannerAdapter<BannerItem>>? = null
    private lateinit var mRecyclerView: RecyclerView
    private var articleAdapter: ArticleAdapter? = null
    private var swipeRefreshLayout: SmartRefreshLayout? = null

    // 当前页数
    private var mCurPage = 0
    private var mPageCount = 0
    private var isLoadingMore = false

    private var bannerUrls: MutableList<String> = ArrayList()
    private var bannerTitles: MutableList<String> = ArrayList()

    override val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.build { HomeViewModel() }
    }

    override fun initViewModel() {
        super.initViewModel()

        viewModel.articalLiveData.observe(this, Observer {
            launchIO {
                val articleList = it.datas
                //当前数据为空时
                if(articleAdapter?.getDefItemCount() == 0 && it?.datas != null){
                    withContext(Dispatchers.Main) {
                        articleAdapter?.addData(articleList!!)
                    }
                    return@launchIO
                }
                // 数据非空时
                val currentFirstData = articleAdapter?.getItem(0)
                if (it?.datas != null) {
                    swipeRefreshLayout?.finishRefresh()
                    if (currentFirstData?.id != articleList!![0].id) {
                        //当前第一个数据不同于接口第一个，表示有新数据
                        ZLog.d("Has new article data.")
                        withContext(Dispatchers.Main) {
                            articleAdapter?.apply {
                                if (!isLoadingMore) {
                                    addData(articleList)
                                } else {
                                    isLoadingMore = false
                                    val defItemCount = getDefItemCount()
                                    addData(defItemCount - 1, articleList)
                                }
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "当前数据已是最新", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "数据请求异常", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        viewModel.bannerLiveData.observe(this, Observer { result ->
            ZLog.d("Banner data onChanged() data size = " + result.data?.size)
            bannerUrls.clear()
            bannerTitles.clear()
            result.data?.forEach { item ->
                bannerUrls.add(item.imagePath)
                bannerTitles.add(Html.fromHtml(item.title).toString())
            }
            banner?.apply {
                setDatas(result.data)
            }
        })
    }

    override fun onViewCreated() {
        //doNothing
        ZLog.d("HomeWanAndroidFragment onViewCreated")
    }

    override fun getLayoutResID() = R.layout.fragment_common_page

    override fun initData(savedInstanceState: Bundle?) {
        if (articleAdapter == null) {
            ZLog.d("articleAdapter == null, getBanner.")
            viewModel.getBanner()
            articleAdapter = ArticleAdapter()
            articleAdapter?.apply {
                adapterAnimation = SlideInBottomAnimation()
                setOnItemClickListener { _, _, position ->
                    if (articleAdapter!!.getDefItemCount() > position) {
                        openWeb(articleAdapter?.getItem(position))
                    }
                }
            }
            requestArticalData(0)
        }
        mRecyclerView.adapter = articleAdapter
    }

    override fun initView(savedInstanceState: Bundle?) {
        mRecyclerView = rootView?.findViewById(R.id.recycler_view) as RecyclerView
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        initSwipeRefreshLayout()
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout = rootView?.findViewById(R.id.swipeRefreshLayout)!!
        swipeRefreshLayout?.apply {
            setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
                override fun onRefresh(refreshLayout: RefreshLayout) {
                    fireVibrate()
                    requestArticalData(0)
                }

                override fun onLoadMore(refreshLayout: RefreshLayout) {
                    if(!isLoadingMore){
                        requestArticalData(mPageCount)
                    }
//                finishLoadMore(2000/*,false*/)//传入false表示加载失败
                }
            })
        }
    }

    /**
     * 触发震动效果
     */
    private fun fireVibrate() {
        val vibrator = context?.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        if (SDKVersion.afterOreo()) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(40)
        }
    }

    private fun requestArticalData(pageId: Int){
        ZLog.d("requestArticalData pageID: $pageId")
        mCurPage = pageId
        mPageCount = if(mCurPage > mPageCount) mCurPage else mPageCount++
        viewModel.getArticalData(pageId)
    }

    private fun openWeb(data: Article?) {
        ARouter.getInstance()
            .build(RPath.PAGE_WEB)
            .withString(WADConstants.KEY_URL, data?.link)
            .withString(WADConstants.KEY_TITLE, data?.title)
            .withString(WADConstants.KEY_AUTHOR, data?.author)
            .withBoolean(WADConstants.KEY_IS_COLLECT, data?.isCollect ?: false)
            .withInt(WADConstants.KEY_ID, data?.id ?: -1)
            .navigation(activity, 0x01)
    }

    private fun openWeb(url: String?) {
        ARouter.getInstance()
            .build(RPath.PAGE_WEB)
            .withString(WADConstants.KEY_URL, url)
            .navigation(activity, 0x01)
    }

    override fun onViewDestory() {
        lifecycleScope.cancel()
        super.onViewDestory()
    }

    override fun onDestroy() {
        super.onDestroy()
        ZLog.d("release.")
        swipeRefreshLayout?.setOnRefreshListener(null)
        swipeRefreshLayout = null
        articleAdapter = null
    }

    override fun onDetach() {
        ZLog.d("Detach fragment.")
        super.onDetach()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("SAVE_INSTANCE_STATE")) {
                //                lazyLoadData();
            }
        }
        initBanner()
    }

    /**
     * 初始化Banner控件
     */
    private fun initBanner() {
        @Suppress("UNCHECKED_CAST")
        banner = LayoutInflater.from(context).inflate(
            R.layout.layout_banner, mRecyclerView, false
        ) as Banner<BannerItem, HomeBannerAdapter<BannerItem>>?
        banner?.apply {
            addBannerLifecycleObserver(activity)
            adapter = HomeBannerAdapter(null)
//            setLoopTime(3000L) //  设置轮播间隔时间 默认3000ms
//            scrollTime = 1000  //  设置轮播间隔时间 默认600ms
            setPageTransformer(ScaleInTransformer())
            indicator = CircleIndicator(context)
            setOnBannerListener { data, _ ->
                openWeb((data as BannerItem).url)
            }
        }
        articleAdapter?.setHeaderView(view = banner!!)
    }
}