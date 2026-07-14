<template>
  <view class="container page">
    <swiper class="banner card" circular autoplay indicator-dots>
      <swiper-item>
        <image class="banner-image" src="/static/specialty-1.jpg" mode="aspectFill" />
      </swiper-item>
      <swiper-item>
        <image class="banner-image" src="/static/specialty-2.jpg" mode="aspectFill" />
      </swiper-item>
    </swiper>
    <view class="card body">
      <text class="title">??????</text>
      <text class="desc">???????????????????????????</text>
      <view class="actions"><text class="tag">????</text><text class="tag">????</text></view>
    </view>
  </view>
</template>
<style lang="scss">
@import '@/styles/theme.scss';
.page{padding:24rpx}.banner{height:360rpx;overflow:hidden}.banner-image{width:100%;height:100%}.body{padding:24rpx;margin-top:18rpx}.title{font-size:36rpx;font-weight:800;display:block;margin-bottom:12rpx}.desc{color:$text-secondary;line-height:1.7;display:block}.actions{margin-top:18rpx;display:flex;gap:12rpx;flex-wrap:wrap}.tag{padding:10rpx 18rpx;border-radius:999rpx;background:$primary-light;color:$primary}
</style>