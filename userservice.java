@Service
public class UserService {

  @Autowired
  private JobLauncher jobLauncher;

  public void startJob() throws JobExecutionException, InterruptedException {
    JobParameters params = new JobParametersBuilder().addString("source", "API").toJobParameters();
    jobLauncher.run(null, params);
  }

  public void stopJob() {
    // Implement logic to stop the running job (if possible)
  }
}
