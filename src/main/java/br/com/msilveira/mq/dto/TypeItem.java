package br.com.msilveira.mq.dto;

public enum TypeItem {
	
	PIZZA() {
		@Override
		public String toString() {
			return "pizza";
		}
	},
	HOT_DOG() {
		@Override
		public String toString() {
			return "hotdog";
		}
	}

}
