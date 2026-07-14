<template>
  <view class="container page">
    <image class="cover card" src="/static/activity.jpg" mode="aspectFill" />
    <view class="card body">
      <text class="title">????????</text>
      <text class="desc">????????????????</text>
      <view class="action"><text class="tag" @click="goSignup">????</text></view>
    </view>
  </view>
</template>
<script setup>
const goSignup = () => uni.navigateTo({ url: '/pages/activity/signup' })
</script>
<style lang="scss">@import '@/styles/theme.scss';.page{padding:24rpx}.cover{width:100%;height:360rpx}.body{padding:24rpx;margin-top:18rpx}.title{font-size:36rpx;font-weight:800;display:block;margin-bottom:12rpx}.desc{color:$text-secondary;line-height:1.7;display:block}.action{margin-top:18rpx}.tag{padding:12rpx 20rpx;border-radius:999rpx;background:$primary;color:#fff;display:inline-flex}</style>