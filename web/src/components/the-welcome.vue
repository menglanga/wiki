<template>
    <div>
        <a-row>
            <a-col :span="24">
                <a-card>
                    <a-row>
                        <a-col :span="8">
                            <a-statistic title="总阅读量" :value="statistic.viewCount">
                                <template #suffix>
                                    <UserOutLined/>
                                </template>
                            </a-statistic>
                        </a-col>
                        <a-col :span="8">
                            <a-statistic title="总点赞量" :value="statistic.voteCount">
                                <template #suffix>
                                    <like-OutLined/>
                                </template>
                            </a-statistic>
                        </a-col>
                        <a-col :span="8">
                            <a-statistic title="点赞率" :value="statistic.voteCount/statistic.viewCount *100"
                            :precision="2" suffix="%" :value-style="{color: 'red'}">
                                <template #suffix>
                                    <like-OutLined/>
                                </template>
                            </a-statistic>
                        </a-col>
                    </a-row>
                </a-card>
            </a-col>
        </a-row>
        <br>
        <a-row :gutter="16">
            <a-col :span="12">
                <a-card>
                    <a-row>
                        <a-col :span="12">
                            <a-statistic title="今日阅读量" :value="statistic.todayViewCount" style="margin-right: 50px">
                                <template #suffix>
                                    <UserOutLined/>
                                </template>
                            </a-statistic>
                        </a-col>
                        <a-col :span="12">
                            <a-statistic title="今日点赞量" :value="statistic.todayVoteCount">
                                <template #suffix>
                                    <like-OutLined/>
                                </template>
                            </a-statistic>
                        </a-col>
                    </a-row>
                </a-card>
            </a-col>
            <a-col :span="12">
                <a-card>
                    <a-row>
                        <a-col :span="12">
                            <a-statistic title="预计今日阅读量" :value="statistic.todayViewIncrease"
                                         :value-style="{color:'blue'}">
                                <template #suffix>
                                    <UserOutLined/>
                                </template>
                            </a-statistic>
                        </a-col>
                        <a-col :span="12">
                            <a-statistic title="预计今日阅读增长量" :value="statistic.todayViewIncreaseRateAbs"
                                         :precision="2" suffix="%" class="demo-class"
                                         :value-style="statistic.todayViewIncreaseRate<0?{color:'green'}:{color:'red'}">
                                <template #prefix>
                                    <arror-down-outlined v-if="statistic.todayViewIncreaseRate<0"/>
                                    <arror-up-outlined v-if="statistic.todayViewIncreaseRate>0"/>
                                </template>
                            </a-statistic>
                        </a-col>
                    </a-row>
                </a-card>
            </a-col>
        </a-row>
        <br>
        <a-row>
            <a-col :span="24">
                <div id="main" style="width:100%; height:300px"></div>
            </a-col>
        </a-row>
    </div>
</template>

<script lang="ts">
    import { defineComponent ,ref,onMounted} from 'vue';
    import axios from 'axios';

    declare let echarts: any;


    export default defineComponent({
        name: 'the-welcome',
        setup(){
            const statistic=ref();
            statistic.value={};
            const getStatistic=()=>{
              axios.get('/ebook-snapshot/get-statistic').then((response)=>{
                  const data=response.data;
                  if (data.success) {
                      const statisticResponse = data.data;
                      statistic.value.viewCount = statisticResponse[1].viewCount;
                      statistic.value.voteCount = statisticResponse[1].voteCount;
                      statistic.value.TodayViewCount = statisticResponse[1].viewIncrease;
                      statistic.value.TodayvoteCount = statisticResponse[1].voteIncrease;

                      //按分钟计算当前时间点占全天的百分比
                      const now = new Date();
                      const nowRate = (now.getHours() * 60 + now.getMinutes()) / (60 * 24);
                      //预计今日阅读量
                      statistic.value.todayViewIncrease =
                          parseInt(String(statisticResponse[1].viewIncrease / nowRate));
                      //预计今日阅读增长率
                      statistic.value.todayViewIncreaseRate =
                          (statistic.value.todayViewIncrease - statisticResponse[0].viewIncrease)
                          / statisticResponse[0].viewIncrease * 100;
                      statistic.value.todayViewIncreaseRateAbs=Math.abs(statistic.value.todayViewIncreaseRate);
                  }
              })
            };

            const testEcharts=()=>{
                // 基于准备好的dom，初始化echarts实例
                const myChart = echarts.init(document.getElementById('main'));

                // 指定图表的配置项和数据
                const option = {
                    title: {
                        text: 'ECharts 入门示例'
                    },
                    tooltip: {},
                    legend: {
                        data: ['销量']
                    },
                    xAxis: {
                        data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子']
                    },
                    yAxis: {},
                    series: [
                        {
                            name: '销量',
                            type: 'bar',
                            data: [5, 20, 36, 10, 10, 20]
                        }
                    ]
                };

                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            };


            onMounted(()=>{
                getStatistic();
                testEcharts();
            });



            return{
              statistic
            }
        }
    });
</script>