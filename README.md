## wzclouds-backend 项目组成
| 项目 | gitee | github | 备注 |
|---|---|---|---|
| 公共包 | - | https://github.com/wzclouds/wzclouds-commons | 业务无关的公共包，微服务业务依赖 |
| 微服务业务包 | - | https://github.com/wzclouds/wzclouds-backend | SpringCloud 微服务 |
| VUE前端UI | - | https://github.com/wzclouds/wzclouds-ui | 项目前端UI|

# wzclouds-backend 简介
`wzclouds-backend` 是基于SpringCloud开发的一套SaaS系统,具备RBAC功能、网关统一鉴权、Xss防跨站攻击、自动代码生成、多种存储系统、分布式事务、分布式定时任务等多个模块，支持多业务系统并行开发， 支持多服务并行开发，可以作为后端服务的开发脚手架。代码简洁，注释齐全，架构清晰，非常适合学习和企业作为基础框架使用。

核心技术采用Spring Cloud Alibaba、SpringBoot、Mybatis、Seata、Sentinel、RabbitMQ、FastDFS/MinIO、SkyWalking等主要框架和中间件。
希望能努力打造一套从 `JavaWeb基础框架` - `分布式微服务架构` - `持续集成` - `系统监测` 的解决方案。`本项目旨在实现基础能力，不涉及具体业务。`

## wzclouds-cloud/wzclouds-backend + wzclouds-ui 业务功能介绍：
1. wzclouds-online: 远程交流系统，是一个结合各类实时通讯的服务平台，包含视频会议，白板，附件，房间管理等功能
2. 后续功能逐步添加中...

## lamp 会员版项目演示地址
- 改造的前端项目演示地址： https://192.168.1.124/wzclouds/meeting#/
