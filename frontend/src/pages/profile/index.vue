<template>
  <view class="container page">
    <view class="head card">
      <image class="avatar" src="/static/profile.jpg" mode="aspectFill" />
      <view>
        <text class="name">????</text>
        <text class="role">???</text>
      </view>
    </view>
    <view class="card menu" @click="jump('/pages/activity/signup')">????</view>
    <view class="card menu" @click="jump('/pages/merchant/index')">????</view>
    <view class="card menu" @click="jump('/pages/admin/index')">????</view>
  </view>
</template>
<script setup>
const jump = (url) => uni.navigateTo({ url })
</script>
<style lang="scss">@import '@/styles/theme.scss';.page{padding:24rpx}.head{padding:24rpx;display:flex;gap:20rpx;align-items:center}.avatar{width:96rpx;height:96rpx;border-radius:50%}.name{font-size:32rpx;font-weight:700;display:block}.role{color:$text-secondary;font-size:24rpx}.menu{padding:24rpx;margin-top:18rpx;font-weight:700}</style>