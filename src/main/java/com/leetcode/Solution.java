package com.leetcode;

import java.util.LinkedList;

public class Solution {

    /**
     * 1. 两数之和
     * @param nums
     * @param target
     * @return
     */
    public int[] twoSum(int[] nums, int target) {

        int[] a = new int[2];

        for (int i = 0 ; i < nums.length ; i ++){
            for (int j = 0 ; j < nums.length ; j ++)
                if (nums[i] + nums[j] == target && i != j ){
                    a[0] = i;
                    a[1] = j;
                    System.out.println("factory[0]:"+a[0]+" factory[i]:"+a[1]);
                }
        }

        return a;

    }

    /**
     * 移动零
     * @param nums
     */
    public void moveZeroes(int[] nums) {
        for(int i = 0 ; i < nums.length ; i ++){
            for(int j = nums.length - 1 ; j > 0 ; j --){
                if(nums[i] == 0 && nums[j] != 0){
                    int t;
                    t = nums[i];
                    nums[i] = nums[j];
                    nums[j] = nums[i];
                }
            }
        }
        System.out.println(nums);
    }


    /**
     * 盛最多水的容器
     * @param height
     * @return
     */
    public int maxArea(int[] height) {

        int area = 0;

        for (int i=0 ; i < height.length ; i++){
            for (int j = i ; j < height.length ; j++){
                int a = (j - i) * Math.min(height[i],height[j]);

                if (a > area)
                    area = a;
            }
        }
        return area;

    }

    /**
     * 70. 爬楼梯
     * @param n
     * @return
     */
    public int climbStairs(int n) {

        int[] f = new int[n+1];

        if(n <= 2) return n;

        f[1] = 1;
        f[2] = 2;

        for(int i = 3 ; i <= n ; i++){
            f[i] = f[i - 1] +f[i - 2];
        }

        return f[n];

    }


    public static void main(String[] args){


        Solution solution = new Solution();

        int [] nums = {3,3};
        System.out.println(solution.climbStairs(2));



        ThreadLocal<Solution> s = new ThreadLocal<>();
        s.set(new Solution());
    }


}
