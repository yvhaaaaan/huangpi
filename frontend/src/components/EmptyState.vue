<template>
  <view class="empty card">
    <text class="icon">{{ icon }}</text>
    <text class="title">{{ title }}</text>
    <text class="desc">{{ desc }}</text>
  </view>
</template>
<script setup>
defineProps({ icon: { type: String, default: '??' }, title: { type: String, default: '????' }, desc: { type: String, default: '??????' } })
</script>
<style lang="scss">
@import '@/styles/theme.scss';
.empty{padding:60rpx 24rpx;text-align:center}.icon{display:block;font-size:72rpx;margin-bottom:16rpx}.title{display:block;font-size:32rpx;font-weight:700;margin-bottom:8rpx}.desc{color:$text-secondary;font-size:24rpx}
</style>
