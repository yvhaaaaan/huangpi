<template>
  <view class="page-wrap home-page">
    <view class="hero card">
      <view class="hero-top">
        <view>
          <view class="subtitle">黄陂镇油茶客家文旅非遗</view>
          <view class="title">探寻油茶文化 · 体验客家风情</view>
        </view>
        <view class="hero-badge">文旅导览</view>
      </view>

      <view class="search-bar" @click="goSearch">
        <text class="search-placeholder">搜索景点 / 特产 / 活动</text>
      </view>

      <swiper class="banner" circular autoplay indicator-dots>
        <swiper-item v-for="item in banners" :key="item.id">
          <image class="banner-image" :src="item.image" mode="aspectFill" />
        </swiper-item>
      </swiper>
    </view>

    <view class="section-head">
      <text class="section-kicker">核心功能</text>
      <text class="section-sub">一屏直达油茶、特产、导览、活动</text>
    </view>
    <view class="card quick-grid">
      <view class="grid-item" v-for="item in shortcuts" :key="item.text" @click="jump(item)">
        <view class="grid-icon">{{ item.icon }}</view>
        <text>{{ item.text }}</text>
      </view>
    </view>

    <view class="section-head">
      <text class="section-kicker">推荐内容</text>
      <text class="section-sub">精选展示黄陂镇特色资源</text>
    </view>
    <view class="section-title">
      <text>推荐油茶与特产</text>
      <text class="muted" @click="jumpTab('/pages/specialty/index')">查看更多</text>
    </view>
    <scroll-view scroll-x class="h-list">
      <view class="mini-card card" v-for="item in specialties" :key="item.id">
        <image class="mini-image" :src="item.image" mode="aspectFill" />
        <view class="mini-body">
          <text class="mini-title">{{ item.title }}</text>
          <text class="mini-desc">{{ item.desc }}</text>
        </view>
      </view>
    </scroll-view>

    <view class="section-head">
      <text class="section-kicker">文旅打卡</text>
      <text class="section-sub">把点位和路线结合起来看</text>
    </view>
    <view class="section-title">
      <text>推荐文旅点位</text>
      <text class="muted" @click="jumpTab('/pages/map/index')">查看更多</text>
    </view>
    <view class="list-card card" v-for="item in points" :key="item.id">
      <image class="list-image" :src="item.image" mode="aspectFill" />
      <view class="list-body">
        <text class="list-title">{{ item.title }}</text>
        <text class="list-desc">{{ item.desc }}</text>
        <view class="tag-row"><text class="tag">{{ item.tag }}</text></view>
      </view>
    </view>

    <view class="section-head">
      <text class="section-kicker">活动报名</text>
      <text class="section-sub">非遗体验、研学与文化活动</text>
    </view>
    <view class="section-title">
      <text>最新活动</text>
      <text class="muted" @click="jumpTab('/pages/activity/index')">查看更多</text>
    </view>
    <view class="activity card" v-for="item in activities" :key="item.id">
      <view class="activity-left">
        <text class="activity-title">{{ item.title }}</text>
        <text class="activity-meta">{{ item.time }} · {{ item.place }}</text>
      </view>
      <view class="activity-status">报名中</view>
    </view>
  </view>
</template>

<script setup>
const banners = [
  { id: 1, image: '/static/home-banner.jpg' },
  { id: 2, image: '/static/home-banner.jpg' },
  { id: 3, image: '/static/home-banner.jpg' }
]

const shortcuts = [
  { text: '油茶文化', icon: '茶', url: '/pages/tea/detail' },
  { text: '油茶与特产', icon: '特', url: '/pages/specialty/index' },
  { text: '客家文旅', icon: '旅', url: '/pages/culture/index' },
  { text: '非遗展示', icon: '遗', url: '/pages/intangible/index' },
  { text: '地图导览', icon: '图', url: '/pages/map/index' },
  { text: '活动资讯', icon: '活', url: '/pages/activity/index' }
]

const specialties = [
  { id: 1, title: '黄陂油茶礼盒', desc: '山野油茶风味，伴手礼推荐', image: '/static/specialty-1.jpg' },
  { id: 2, title: '客家手作点心', desc: '本地特色，传统手艺制作', image: '/static/specialty-2.jpg' },
  { id: 3, title: '非遗文创纪念品', desc: '融合文化元素的特色礼品', image: '/static/profile.jpg' }
]

const points = [
  { id: 1, title: '油茶体验点', desc: '可了解油茶制作与品鉴', tag: '油茶', image: '/static/map-point.jpg' },
  { id: 2, title: '客家古村落', desc: '感受传统客家建筑与生活', tag: '文旅', image: '/static/profile.jpg' },
  { id: 3, title: '非遗工坊', desc: '体验传统技艺与手工制作', tag: '非遗', image: '/static/activity.jpg' }
]

const activities = [
  { id: 1, title: '油茶文化体验活动', time: '10月12日', place: '黄陂镇文化广场' },
  { id: 2, title: '客家非遗手作课堂', time: '10月18日', place: '非遗工坊' }
]

const goSearch = () => uni.navigateTo({ url: '/pages/common/search' })

const jump = (item) => {
  if (item.url === '/pages/home/index') return
  if (item.url.startsWith('/pages/common')) {
    uni.navigateTo({ url: item.url })
    return
  }
  if (item.url.startsWith('/pages/tea') || item.url.startsWith('/pages/culture') || item.url.startsWith('/pages/intangible')) {
    uni.navigateTo({ url: item.url })
    return
  }
  uni.switchTab({ url: item.url })
}

const jumpTab = (url) => uni.switchTab({ url })
</script>

<style lang="scss">
@import '@/styles/theme.scss';

.page-wrap {
  min-height: 100vh;
  padding: 24rpx;
  box-sizing: border-box;
}

.hero {
  padding: 28rpx;
  background: linear-gradient(180deg, rgba(232, 244, 237, 0.95), rgba(255, 255, 255, 0.98));
}

.hero-top {
  display: flex;
  justify-content: space-between;
  align-items: start;
  gap: 16rpx;
}

.subtitle {
  color: $primary;
  font-size: 24rpx;
  margin-bottom: 8rpx;
}

.title {
  font-size: 42rpx;
  font-weight: 800;
  line-height: 1.25;
  color: #173426;
}

.hero-badge {
  padding: 10rpx 18rpx;
  border-radius: 999rpx;
  background: rgba(46, 107, 78, 0.12);
  color: $primary;
  font-size: 24rpx;
}

.search-bar {
  margin: 20rpx 0 22rpx;
  padding: 22rpx 24rpx;
  border-radius: 999rpx;
  background: rgba(255, 255, 255, 0.9);
  color: #91a39a;
  border: 1rpx solid rgba(46, 107, 78, 0.12);
}

.banner {
  height: 300rpx;
  border-radius: 24rpx;
  overflow: hidden;
  box-shadow: 0 18rpx 30rpx rgba(46, 107, 78, 0.1);
}

.banner-image {
  width: 100%;
  height: 100%;
}

.section-head {
  margin: 28rpx 0 14rpx;
}

.section-kicker {
  display: block;
  font-size: 24rpx;
  color: #2e6b4e;
  font-weight: 700;
  letter-spacing: 2rpx;
}

.section-sub {
  display: block;
  margin-top: 6rpx;
  font-size: 24rpx;
  color: #7a877f;
}

.quick-grid {
  padding: 24rpx;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20rpx;
}

.grid-item {
  text-align: center;
  padding: 12rpx 0;
}

.grid-icon {
  width: 88rpx;
  height: 88rpx;
  line-height: 88rpx;
  margin: 0 auto 12rpx;
  border-radius: 50%;
  background: linear-gradient(180deg, #edf7f0, #dbede3);
  color: $primary;
  font-weight: 700;
  box-shadow: inset 0 0 0 1rpx rgba(46, 107, 78, 0.06);
}

.h-list {
  white-space: nowrap;
  width: 100%;
}

.mini-card {
  display: inline-block;
  width: 320rpx;
  margin-right: 18rpx;
  overflow: hidden;
  border-radius: 22rpx;
}

.mini-image,
.list-image {
  width: 100%;
  height: 180rpx;
  background: #eef3ef;
}

.mini-body,
.list-body {
  padding: 18rpx;
}

.mini-title,
.list-title,
.activity-title {
  display: block;
  font-weight: 700;
  margin-bottom: 8rpx;
}

.mini-desc,
.list-desc,
.activity-meta {
  color: $text-secondary;
  font-size: 24rpx;
  line-height: 1.5;
}

.list-card {
  margin-bottom: 18rpx;
  overflow: hidden;
  border-radius: 22rpx;
}

.tag-row {
  margin-top: 12rpx;
}

.activity {
  padding: 22rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 18rpx;
  border-radius: 22rpx;
}

.activity-status {
  padding: 10rpx 16rpx;
  border-radius: 999rpx;
  background: #fff2ea;
  color: $accent;
  font-size: 24rpx;
}
</style>
