<template>
	<div style="margin: 0px;height: 100%;width:100%;">
		<div class='jiesuan_top_area'>
			<!-- top -->
		</div>
		<div class='jiesuan_middle_area '>
			2222222222
		</div>
		<router-link :to="{ path: '/convertibleDetail/1' }" class="jiesuan_bottom_area duihuan_btn"><span>
				兑换</span></router-link>
	</div>
</template>

<script>
	export default {
		created: function() {},
		// 		data() {
		// 			return {
		// 				transition: "slide-left"
		// 
		// 			};
		// 		},
		// 		watch: {
		// 			$route(to, from) {
		// 				var back = this.$router.back;
		// 				if (back) { //点击了返回
		// 					this.transition = 'slide-right'
		// 				} else {
		// 					this.transition = 'slide-left'
		// 				}
		// 				this.$router.back = false;
		// 			}
		// 		},
		methods: {

		}
	}
</script>

<style>
	.jiesuan_top_area {
		z-index: 1;
		position: fixed;
		top: 0vh;
		width: 100%;
		height: 6.85vh;
		color: #fff;
		background-color: green;
		text-align: center;
	}

	.jiesuan_middle_area {
		background-color: red;
		position: fixed;
		width: 100%;
		z-index: 0;
		top: 6.85vh;
		bottom: 7.15vh;
	}

	.jiesuan_bottom_area {
		z-index: 1;
		position: fixed;
		bottom: 0vh;
		width: 100%;
		height: 7.15vh;
		color: #fff;
		background-color: cornflowerblue;
		text-align: center;
	}

	/*兑换按钮*/
	/* 兑换按钮--start */
	.duihuan_btn {
		/* width: 50%;
		height: 62.5%; */
		background: #007AFF;
		border: 1px solid #007AFF;
		/* border-radius: 4px; */
		color: #ffffff;
		box-shadow: 0px 0px 0px gray;
	}

	.duihuan_btn:hover {
		background-color: #009CFF;
	}

	/*active 要在后面 否则会被hover覆盖*/
	.duihuan_btn:focus {
		background-color: #009CFF;
	}

	.duihuan_btn:active {
		background-color: #009CFF;
	}

	.duihuan_btn>span {
		height: 100%;
		color: white;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 1.25rem;
	}
</style>
