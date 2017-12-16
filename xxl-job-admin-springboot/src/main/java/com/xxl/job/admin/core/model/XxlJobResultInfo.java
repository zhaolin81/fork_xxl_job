package com.xxl.job.admin.core.model;

/**
 * 批量暂停/恢复任务接口反馈结果
 * Created by zhaolin on 9/25/2017.
 */
public class XxlJobResultInfo {
    private int id;				// 主键ID	    (JobKey.name)
    private int jobGroup;		// 执行器主键ID	(JobKey.group)
    private String jobDesc;
    private boolean result;    //执行结果

    public static XxlJobResultInfo buildResultInfo(XxlJobInfo xxlJobInfo, boolean result){
        XxlJobResultInfo xxlJobResultInfo = new XxlJobResultInfo();
        xxlJobResultInfo.setId(xxlJobInfo.getId());
        xxlJobResultInfo.setJobGroup(xxlJobInfo.getJobGroup());
        xxlJobResultInfo.setJobDesc(xxlJobInfo.getJobDesc());
        xxlJobResultInfo.setResult(result);
        return xxlJobResultInfo;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(int jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XxlJobResultInfo that = (XxlJobResultInfo) o;

        if (id != that.id) return false;
        if (jobGroup != that.jobGroup) return false;
        if (result != that.result) return false;
        return jobDesc != null ? jobDesc.equals(that.jobDesc) : that.jobDesc == null;
    }

    @Override
    public int hashCode() {
        int result1 = id;
        result1 = 31 * result1 + jobGroup;
        result1 = 31 * result1 + (jobDesc != null ? jobDesc.hashCode() : 0);
        result1 = 31 * result1 + (result ? 1 : 0);
        return result1;
    }
}
