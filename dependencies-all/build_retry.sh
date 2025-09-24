#!/bin/bash

# 定义最大重试次数（可选，防止无限循环）
MAX_RETRIES=20
RETRY_COUNT=0

while true; do
    echo "执行 mvn clean compile -U (第 $((RETRY_COUNT + 1)) 次尝试)..."

    # 执行 Maven 命令
    #mvn clean compile -U
    mvn clean compile -U dependency:sources dependency:resolve -Dclassifier=javadoc

    # 检查命令是否成功
    if [ $? -eq 0 ]; then
        echo "构建成功！"
        break  # 成功则退出循环
    else
        RETRY_COUNT=$((RETRY_COUNT + 1))
        if [ $RETRY_COUNT -ge $MAX_RETRIES ]; then
            echo "错误：已达到最大重试次数 ($MAX_RETRIES)，退出。"
            exit 1
        fi
        echo "构建失败，$((5 * 60)) 秒后重试..."
        sleep $((5 * 60))  # 休眠 5 分钟
    fi
done
