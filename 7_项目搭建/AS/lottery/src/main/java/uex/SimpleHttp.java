package uex;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author wx
 *
 */
public class SimpleHttp {

	public static final int TIME_OUT = 15000;
	/**
	 * File buffer stream size.
	 */
	public static final int FILE_STREAM_BUFFER_SIZE = 8192;

	public static Result get(final String path, final String param) {
		return new SimpleHttp().getToServer(path, param);
	}

	public static Result post(final String path, final String param) {
		return new SimpleHttp().postToServer(path, param);
	}

	public static Result postJson(final String path, final String param) {
		return new SimpleHttp().postToServerJson(path, param);
	}

	public static Result post30Delay(final String path, final String param) {
		return new SimpleHttp().postToServer30Delay(path, param);
	}

	public static Result netTest() {

		return null;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// Result
					// rs=post("http://127.0.0.1:7931/demo/reg","userid=1");
					// Result
					// rs=post("http://127.0.0.1.228:7931/demo/xy","companyid=c1");
					// Result
					// rs=post("http://127.0.0.1:7931/demo/checkin","userid=119");

					Result rs = post("http://220.169.30.228:7931/demo/reg",
							"userid=180");
					// Result
					// rs=post("http://220.169.30.228:7931/demo/xy","companyid=c1");
					// Result
					// rs=post("http://220.169.30.228:7931/demo/checkin","userid=333");
					System.out.println(rs.returnCode + " " + rs.retString);
				}
			}).start();

		}
	}

	private Result postToServerJson(final String path, final String param) {
		ExecutorService es = Executors.newSingleThreadExecutor();
		Result r = new Result();
		Future<String> f = null;
		//
		try {
			Callable<String> task = new Callable<String>() {

				@Override
				public String call() throws Exception {

					HttpURLConnection con = null;
					InputStream in = null;
					OutputStream os = null;
					try {
						URL url = new URL(path);
						con = (HttpURLConnection) url.openConnection();
						con.setRequestProperty("Content-Type",
								"application/json");
						final int timeout = TIME_OUT;
						con.setConnectTimeout(timeout);
						con.setReadTimeout(timeout);
						con.setDoInput(true);
						con.setDoOutput(true);
						con.setUseCaches(false);
						con.setRequestMethod("POST");
						//
						os = con.getOutputStream();
						os.write(param.getBytes());
						os.flush();
						in = con.getInputStream();
						String ret = streamToString(in, "UTF-8");
						return ret;
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					} finally {
						close(os);
						close(in);
						if (con != null) {
							con.disconnect();
						}
					}
				}
			};
			f = es.submit(task);
			String ret = f.get(TIME_OUT, TimeUnit.MILLISECONDS);
			if (ret == null)
				r.makeExResult();
			else
				r.makeSucResult(ret);
			return r;
		} catch (ExecutionException eex) {
			eex.printStackTrace();
			r.makeExResult();
			f.cancel(true);
		} catch (TimeoutException tex) {
			tex.printStackTrace();
			r.makeTimeoutResult();
		} catch (Exception e) {
			e.printStackTrace();
			r.makeExResult();
		} finally {
			es.shutdown();
		}
		//
		return r;
	}

	private Result postToServer(final String path, final String param) {
		ExecutorService es = Executors.newSingleThreadExecutor();
		Result r = new Result();
		Future<String> f = null;
		//
		try {
			Callable<String> task = new Callable<String>() {

				@Override
				public String call() throws Exception {

					HttpURLConnection con = null;
					InputStream in = null;
					OutputStream os = null;
					try {
						URL url = new URL(path);
						con = (HttpURLConnection) url.openConnection();
						con.setRequestProperty("Content-Type",
								"application/x-www-form-urlencoded");
						final int timeout = TIME_OUT;
						con.setConnectTimeout(timeout);
						con.setReadTimeout(timeout);
						con.setDoInput(true);
						con.setDoOutput(true);
						con.setUseCaches(false);
						con.setRequestMethod("POST");
						//
						os = con.getOutputStream();
						os.write(param.getBytes());
						os.flush();
						in = con.getInputStream();
						String ret = streamToString(in, "UTF-8");
						return ret;
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					} finally {
						close(os);
						close(in);
						if (con != null) {
							con.disconnect();
						}
					}
				}
			};
			f = es.submit(task);
			String ret = f.get(TIME_OUT, TimeUnit.MILLISECONDS);
			if (ret == null)
				r.makeExResult();
			else
				r.makeSucResult(ret);
			return r;
		} catch (ExecutionException eex) {
			eex.printStackTrace();
			r.makeExResult();
			f.cancel(true);
		} catch (TimeoutException tex) {
			tex.printStackTrace();
			r.makeTimeoutResult();
		} catch (Exception e) {
			e.printStackTrace();
			r.makeExResult();
		} finally {
			es.shutdown();
		}
		//
		return r;
	}

	private Result getToServer(final String path, final String param) {
		ExecutorService es = Executors.newSingleThreadExecutor();
		Result r = new Result();
		Future<String> f = null;
		//
		try {
			Callable<String> task = new Callable<String>() {

				@Override
				public String call() throws Exception {

					HttpURLConnection con = null;
					InputStream in = null;
					OutputStream os = null;
					try {
						URL url = new URL(path + "?" + param);
						con = (HttpURLConnection) url.openConnection();
						con.setRequestProperty("Content-Type",
								"application/x-www-form-urlencoded");
						final int timeout = TIME_OUT;
						con.setConnectTimeout(timeout);
						con.setReadTimeout(timeout);
						con.setDoInput(true);
						// con.setDoOutput(true);
						// con.setUseCaches(false);
						con.setRequestMethod("GET");
						con.connect();
						in = con.getInputStream();
						String ret = streamToString(in, "UTF-8");
						return ret;
					} catch (SocketTimeoutException e) {
						e.printStackTrace();
						return null;
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					} finally {
						close(os);
						close(in);
						if (con != null) {
							con.disconnect();
						}
					}
				}
			};
			f = es.submit(task);
			String ret = f.get(TIME_OUT, TimeUnit.MILLISECONDS);
			if (ret == null)
				r.makeExResult();
			else
				r.makeSucResult(ret);
			return r;
		} catch (ExecutionException eex) {
			eex.printStackTrace();
			r.makeExResult();
			f.cancel(true);
		} catch (TimeoutException tex) {
			tex.printStackTrace();
			r.makeTimeoutResult();
		} catch (Exception e) {
			e.printStackTrace();
			r.makeExResult();
		} finally {
			es.shutdown();
		}
		//
		return r;
	}

	private Result postToServer30Delay(final String path, final String param) {
		ExecutorService es = Executors.newSingleThreadExecutor();
		Result r = new Result();
		Future<String> f = null;
		final int timeout = 30000;

		//
		try {
			Callable<String> task = new Callable<String>() {

				@Override
				public String call() throws Exception {

					HttpURLConnection con = null;
					InputStream in = null;
					OutputStream os = null;
					try {
						URL url = new URL(path);
						con = (HttpURLConnection) url.openConnection();
						con.setRequestProperty("Content-Type",
								"application/x-www-form-urlencoded");
						con.setConnectTimeout(timeout);
						con.setReadTimeout(timeout);
						con.setDoInput(true);
						con.setDoOutput(true);
						con.setUseCaches(false);
						con.setRequestMethod("POST");
						//
						os = con.getOutputStream();
						os.write(param.getBytes());
						os.flush();
						in = con.getInputStream();
						String ret = streamToString(in, "UTF-8");
						return ret;
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					} finally {
						close(os);
						close(in);
						if (con != null) {
							con.disconnect();
						}
					}
				}
			};
			// 鎻愪氦浠诲姟
			f = es.submit(task);
			String ret = f.get(timeout, TimeUnit.MILLISECONDS);
			if (ret == null)
				r.makeExResult();
			else
				r.makeSucResult(ret);
			return r;
		} catch (ExecutionException eex) {
			eex.printStackTrace();
			r.makeExResult();
			f.cancel(true);
		} catch (TimeoutException tex) {
			tex.printStackTrace();
			r.makeTimeoutResult();
		} catch (Exception e) {
			e.printStackTrace();
			r.makeExResult();
		} finally {
			es.shutdown();
		}
		//
		return r;
	}

	// -----------------------Tool
	// functions----------------------------------------

	private static String streamToString(InputStream is, String enc) {
		StringBuilder buffer = new StringBuilder();
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, enc), FILE_STREAM_BUFFER_SIZE);
			while (null != (line = reader.readLine())) {
				buffer.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(is);
		}
		return buffer.toString();
	}

	private static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static class Result {
		public static int CODE_SUCCESS = 0;
		public static int CODE_TIME_OUT = 1;
		public static int CODE_EXCEPTION = 2;

		public static String ERROR_SUCCESS = "ERROR_SUCCESS";
		public static String ERROR_TIME_OUT = "ERROR_TIME_OUT";
		public static String ERROR_EXCEPTION = "ERROR_EXCEPTION";

		public int returnCode;
		public String errorMsg;
		public String retString;

		public Result() {
		}

		public void makeExResult() {
			errorMsg = Result.ERROR_EXCEPTION;
			returnCode = Result.CODE_EXCEPTION;
			retString = null;
		}

		public void makeTimeoutResult() {
			errorMsg = Result.ERROR_TIME_OUT;
			returnCode = Result.CODE_TIME_OUT;
			retString = null;
		}

		public void makeSucResult(String ret) {
			errorMsg = Result.ERROR_SUCCESS;
			returnCode = Result.CODE_SUCCESS;
			retString = ret;
		}

		@Override
		public String toString() {
			return returnCode + "," + errorMsg + "," + retString;
		}
	}
}
