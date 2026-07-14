<template>
  <view class="container page">
    <view class="activity card" v-for="item in activities" :key="item.id">
      <view>
        <text class="title">{{ item.title }}</text>
        <text class="meta">{{ item.time }} ? {{ item.place }}</text>
      </view>
      <view class="btn">???</view>
    </view>
  </view>
</template>
<script setup>
const activities = [
  { id: 1, title: '????????', time: '10?12?', place: '???????' },
  { id: 2, title: '????????', time: '10?18?', place: '????' }
]
</script>
<style lang="scss">
@import '@/styles/theme.scss';
.page{padding:24rpx}.activity{padding:22rpx;margin-bottom:18rpx;display:flex;justify-content:space-between;align-items:center}.title{font-size:30rpx;font-weight:700;display:block;margin-bottom:8rpx}.meta{color:$text-secondary;font-size:24rpx}.btn{background:#fff2ea;color:$accent;padding:10rpx 16rpx;border-radius:999rpx}
</style>