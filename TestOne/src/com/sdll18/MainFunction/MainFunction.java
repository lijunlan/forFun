package com.sdll18.MainFunction;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class MainFunction {

	public static void main(String[] args) {
		System.out.println("请输入要产生的随机数个数(大于0正整数):");
		Scanner scan = new Scanner(System.in);
		int number = 0;
		boolean ok1 = false;
		while (!ok1) {
			try {
				number = scan.nextInt();
				if (number <= 0)
					throw new InputMismatchException();
				ok1 = true;
			} catch (InputMismatchException e) {
				System.out.println("输入不是正整数，请重新输入");
				scan.nextLine();
			}
		}

		System.out.println("请输入产生随机数的范围");
		int start = 0;
		boolean ok2 = false;
		while (!ok2) {
			try {
				start = scan.nextInt();
				ok2 = true;
			} catch (InputMismatchException e) {
				System.out.println("输入不是整数，请重新输入随机数左界");
				scan.nextLine();
			}
		}

		int end = 0;
		boolean ok3 = false;
		while (!ok3) {
			try {
				end = scan.nextInt();
				if (end < start)
					throw new InputMismatchException();
				ok3 = true;
			} catch (InputMismatchException e) {
				System.out.println("输入不是整数或小于开始数，请重新输入随机数右界");
				scan.nextLine();
			}
		}

		
		scan.close();
		Random random = new Random();
		for (int i = 1; i <= number; i++) {
			System.out.print(random.nextInt(end - start) + start);
			System.out.print(" ");
			if (i % 5 == 0)
				System.out.print("\n");
		}
	}
}
