package com.xxl.job.admin.core.thread;

import com.xxl.job.admin.core.status.JobStatusListener;
import com.xxl.job.admin.core.status.annotation.JobStatus;
import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.model.XxlJobLog;
import com.xxl.job.admin.core.schedule.XxlJobDynamicScheduler;
import com.xxl.job.admin.core.util.MailUtil;
import com.xxl.job.core.biz.model.ReturnT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * job monitor instance
 * @author xuxueli 2015-9-1 18:05:56
 */
@Component
public class JobStatusMonitorHelper implements ApplicationContextAware{
	private static Logger logger = LoggerFactory.getLogger(JobStatusMonitorHelper.class);
	
	private static JobStatusMonitorHelper instance = new JobStatusMonitorHelper();
	public static JobStatusMonitorHelper getInstance(){
		return instance;
	}

	private ApplicationContext applicationContext;
	private LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(0xfff8);

	private Thread monitorThread;
	private boolean toStop = false;
	public void start(){

		monitorThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (!toStop) {
					try {
						logger.debug(">>>>>>>>>>> job monitor beat ... ");
						Integer jobLogId = JobStatusMonitorHelper.instance.queue.take();
						if (jobLogId != null && jobLogId > 0) {
							logger.debug(">>>>>>>>>>> job monitor heat success, JobLogId:{}", jobLogId);
							XxlJobLog log = XxlJobDynamicScheduler.xxlJobLogDao.load(jobLogId);
							if (log!=null) {
								XxlJobInfo info = XxlJobDynamicScheduler.xxlJobInfoDao.loadById(log.getJobId());
								if (ReturnT.SUCCESS_CODE==log.getTriggerCode() && log.getHandleCode()==0) {
									// running
									try {
										TimeUnit.SECONDS.sleep(10);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									JobStatusMonitorHelper.monitor(jobLogId);
								}else if (ReturnT.SUCCESS_CODE==log.getTriggerCode() && ReturnT.SUCCESS_CODE==log.getHandleCode()) {
									// pass
									fireJobSuccess(info,log);
								}else if (ReturnT.FAIL_CODE == log.getTriggerCode()|| ReturnT.FAIL_CODE==log.getHandleCode()) {
									if (info!=null && info.getAlarmEmail()!=null && info.getAlarmEmail().trim().length()>0) {

										Set<String> emailSet = new HashSet<String>(Arrays.asList(info.getAlarmEmail().split(",")));
										if(emailSet!=null && !emailSet.isEmpty()) {
											XxlJobGroup group = XxlJobDynamicScheduler.xxlJobGroupDao.load(Integer.valueOf(info.getJobGroup()));
											for (String email : emailSet) {
												String title = "《调度监控报警》(任务调度中心XXL-JOB)";
												String content = MessageFormat.format("任务调度失败, 执行器名称:{0}, 任务描述:{1}.", group != null ? group.getTitle() : "null", info.getJobDesc());
												MailUtil.sendMail(email, title, content, false, null);
											}
										}
									}
									fireJobFail(info,log);
								}
							}
						}
					} catch (Exception e) {
						logger.error("job monitor error:{}", e);
					}
				}
			}
		});
		monitorThread.setDaemon(true);
		monitorThread.start();
	}

	public void fireJobFail(XxlJobInfo jobInfo, XxlJobLog jobLog){
		//jobStatusListener with named executorHandler callback
		JobStatusListener jobStatusListener = jobStatusListenerRepository.get(jobInfo.getExecutorHandler());
		if(jobStatusListener !=null){
			jobStatusListener.onJobFail(jobInfo,jobLog);
		}
		//all jobStatusListener callback
		jobStatusListener = jobStatusListenerRepository.get(JobStatus.ALL);
		if(jobStatusListener !=null){
			jobStatusListener.onJobFail(jobInfo,jobLog);
		}
	}

	public void fireJobSuccess(XxlJobInfo jobInfo, XxlJobLog jobLog){
		//jobStatusListener with named executorHandler callback
		JobStatusListener jobStatusListener = jobStatusListenerRepository.get(jobInfo.getExecutorHandler());
		if(jobStatusListener !=null){
			jobStatusListener.onJobSuccess(jobInfo,jobLog);
		}
		//all jobStatusListener callback
		jobStatusListener = jobStatusListenerRepository.get(JobStatus.ALL);
		if(jobStatusListener !=null){
			jobStatusListener.onJobSuccess(jobInfo,jobLog);
		}
	}


	public void toStop(){
		toStop = true;
		//monitorThread.interrupt();
	}
	
	// producer
	public static void monitor(int jobLogId){
		getInstance().queue.offer(jobLogId);
	}

	// ---------------------------------- job fail status repository
	private static ConcurrentHashMap<String, JobStatusListener> jobStatusListenerRepository = new ConcurrentHashMap<String, JobStatusListener>();
	public static JobStatusListener registJobStatusListener(String name, JobStatusListener jobStatusListener){
		logger.info("xxl-job register JobStatusListener success, name:{}, jobHandler:{}", name, jobStatusListener);
		return jobStatusListenerRepository.put(name, jobStatusListener);
	}

	public static boolean hasJobStatusListenerBean(JobStatusListener jobStatusListener) {
		return jobStatusListenerRepository.containsValue(jobStatusListener);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		// initial jobStatusListener
		Map serviceBeanMap = applicationContext.getBeansWithAnnotation(JobStatus.class);
		String serviceBeanName;
		JobStatusListener serviceBean1;
		if(serviceBeanMap != null && serviceBeanMap.size() > 0) {
			Iterator serviceBean2Map = serviceBeanMap.values().iterator();

			while(serviceBean2Map.hasNext()) {
				Object serviceBean = serviceBean2Map.next();
				if(serviceBean instanceof JobStatusListener) {
					serviceBeanName = ((JobStatus)serviceBean.getClass().getAnnotation(JobStatus.class)).value();
					serviceBean1 = (JobStatusListener)serviceBean;
					registJobStatusListener(serviceBeanName, serviceBean1);
				}
			}
		}

		Map serviceBean2Map1 = applicationContext.getBeansOfType(JobStatusListener.class);
		if(serviceBean2Map1 != null && serviceBean2Map1.size() > 0) {
			Iterator serviceBean2 = serviceBean2Map1.keySet().iterator();

			while(serviceBean2.hasNext()) {
				serviceBeanName = (String)serviceBean2.next();
				serviceBean1 = (JobStatusListener)serviceBean2Map1.get(serviceBeanName);
				if(serviceBeanName.endsWith("Listener")){
					serviceBeanName = serviceBeanName.substring(0,serviceBeanName.length()-"Listener".length());
				}
				if(!hasJobStatusListenerBean(serviceBean1)) {
					registJobStatusListener(serviceBeanName, serviceBean1);
				}
			}
		}
	}
}
