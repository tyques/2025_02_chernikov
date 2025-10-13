package lab.shift.config;

public class CommandLineArgs {
    private final String inputFile;
    private final String outputFile;

    private CommandLineArgs(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public String getInputFile() {
        return inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public static CommandLineArgs parse(String[] args) {
        if (args.length < 4) {
            throw new IllegalArgumentException(
                    "Недостаточно аргументов. Требуется указать входной файл и место вывода.\n" +
                            "Пример: --input input.txt --output CONSOLE\n" +
                            "Или: --input input.txt --output output.txt");
        }

        String inputFile = null;
        String outputFile = null;

        for (int i = 0; i < args.length; i++) {
            if ("--input".equals(args[i]) && i + 1 < args.length) {
                inputFile = args[++i];
            } else if ("--output".equals(args[i]) && i + 1 < args.length) {
                outputFile = args[++i];
            }
        }

        if (inputFile == null || outputFile == null) {
            throw new IllegalArgumentException("Неверные аргументы. Проверьте флаги --input и --output.");
        }

        return new CommandLineArgs(inputFile, outputFile);
    }
}