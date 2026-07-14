<template><view class="container page"><view class="card item" v-for="i in list" :key="i.id"><text class="title">{{i.title}}</text><text class="desc">{{i.desc}}</text></view></view></template>
<script setup>
const list=[{id:1,title:'?????',desc:'?????????'},{id:2,title:'????',desc:'?????????'}]
</script>
<style lang="scss">@import '@/styles/theme.scss';.page{padding:24rpx}.item{padding:22rpx;margin-bottom:18rpx}.title{font-weight:700;display:block;margin-bottom:8rpx}.desc{color:$text-secondary}</style>