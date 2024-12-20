package vlad.gurbatov.socks.configuration;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import vlad.gurbatov.socks.entity.Sock;
import vlad.gurbatov.socks.entity.dto.SockDto;
import vlad.gurbatov.socks.repository.SockRepository;

import java.io.File;

@SpringBootConfiguration
@AllArgsConstructor
public class SpringBatchConfig {
    private final SockRepository sockRepository;
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;

    @Bean
    @StepScope
    public FlatFileItemReader<SockDto> itemReader(@Value("#{jobParameters[fullPathFileName]}") String pathToFile) {
        FlatFileItemReader<SockDto> reader = new FlatFileItemReader<>();
        reader.setName("csvReader");
        reader.setResource(new FileSystemResource(new File(pathToFile)));
        reader.setLinesToSkip(1);
        reader.setLineMapper(lineMapper());
        return reader;
    }

    @Bean
    public SockProcessor processor() {
        return new SockProcessor();
    }

    @Bean
    public RepositoryItemWriter<Sock> writer() {
        RepositoryItemWriter<Sock> writer = new RepositoryItemWriter<>();
        writer.setRepository(sockRepository);
        writer.setMethodName("save");
        return writer;
    }

    public Step importStep(FlatFileItemReader<SockDto> itemReader) {
        return new StepBuilder("csvImport", jobRepository).
                <SockDto, Sock>chunk(10, transactionManager)
                .reader(itemReader)
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job runJob(FlatFileItemReader<SockDto> itemReader) {
        return new JobBuilder("importSocks", jobRepository)
                .start(importStep(itemReader))
                .build();
    }

    private LineMapper<SockDto> lineMapper() {
        DefaultLineMapper<SockDto> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(", ");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("color", "cotton", "amount");

        BeanWrapperFieldSetMapper<SockDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();

        fieldSetMapper.setTargetType(SockDto.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }
}
