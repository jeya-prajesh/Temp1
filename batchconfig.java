@Configuration
@EnableBatchProcessing
public class BatchConfig {

  @Bean
  public Job importUserJob(JobBuilderFactory jobs, Step step1) {
    return jobs.get("importUserJob")
        .flow(step1)
        .end()
        .build();
  }

  @Bean
  public Step step1(StepBuilderFactory stepBuilderFactory,
                   ItemReader<User> reader,
                   ItemProcessor<User, User> processor,
                   ItemWriter<User> writer) {
    return stepBuilderFactory.get("step1")
        .<User, User>chunk(100)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @Bean
  public FlatFileItemReader<User> reader() {
    FlatFileItemReader<User> reader = new FlatFileItemReader<>();
    reader.setResource(new ClassPathResource("data.csv"));
    reader.setLineMapper(new DefaultLineMapper<User>() {{
      setLineTokenizer(new DelimitedLineTokenizer() {{
        setNames(new String[]{"id", "name", "age", "city"});
      }});
      setFieldSetMapper(new BeanWrapperFieldSetMapper<User>() {{
        setTargetType(User.class);
      }});
    }});
    reader.setLinesToSkip(1); // Skip header row
    return reader;
  }

  @Bean
  public ItemProcessor<User, User> processor() {
    return item -> item; // No processing needed in this example
  }

  @Bean
  public JdbcBatchItemWriter<User> writer(DataSource dataSource) {
    JdbcBatchItemWriter<User> writer = new JdbcBatchItemWriter<>();
    writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
    writer.setSql("INSERT INTO USERS (id, name, age, city) VALUES (:id, :name, :age, :city)");
    writer.setDataSource(dataSource);
    return writer;
  }
}
