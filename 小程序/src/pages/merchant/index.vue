<template>
  <view class="container page">
    <view class="head card">
      <image class="avatar" src="/static/profile.jpg" mode="aspectFill" />
      <view>
        <text class="name">????</text>
        <text class="desc">????????????</text>
      </view>
    </view>
    <view class="card item" @click="jump('/pages/specialty/detail')">????</view>
    <view class="card item" @click="jump('/pages/activity/detail')">????</view>
  </view>
</template>
<script setup>
const jump = (url) => uni.navigateTo({ url })
</script>
<style lang="scss">@import '@/styles/theme.scss';.page{padding:24rpx}.head{padding:24rpx;display:flex;gap:20rpx;align-items:center}.avatar{width:96rpx;height:96rpx;border-radius:50%}.name{font-size:32rpx;font-weight:700;display:block}.desc{color:$text-secondary;font-size:24rpx}.item{padding:24rpx;margin-top:18rpx;font-weight:700}</style>