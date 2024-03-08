@RestController
@RequestMapping("/batch")
public class BatchController {

  @Autowired
  private UserService userService;

  @PostMapping("/startbatch")
  public String startBatch() {
    try {
      userService.startJob();
      return "Batch job started successfully";
    } catch (Exception e) {
      return "Error starting batch job: " + e.getMessage();
    }
  }

  @PostMapping("/stopbatch")
  public String stopBatch() {
    userService.stopJob();
    return "Batch job stopped (implementation required)";
  }
}
