<template>
  <view class="container page">
    <swiper class="banner card" circular autoplay indicator-dots>
      <swiper-item v-for="img in images" :key="img">
        <image class="banner-image" :src="img" mode="aspectFill" />
      </swiper-item>
    </swiper>
    <view class="card body">
      <text class="title">??????</text>
      <text class="desc">???????????????????????</text>
      <view class="tags"><text class="tag">????</text><text class="tag">????</text></view>
    </view>
  </view>
</template>
<script setup>
const images = ['/static/home-banner.jpg', '/static/map-point.jpg', '/static/activity.jpg']
</script>
<style lang="scss">
@import '@/styles/theme.scss';
.page{padding:24rpx}.banner{height:380rpx;overflow:hidden}.banner-image{width:100%;height:100%}.body{padding:24rpx;margin-top:18rpx}.title{font-size:36rpx;font-weight:800;display:block;margin-bottom:12rpx}.desc{color:$text-secondary;line-height:1.7;display:block}.tags{margin-top:16rpx;display:flex;gap:12rpx;flex-wrap:wrap}
</style>