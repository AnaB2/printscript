import commands.Command

class CLIInvoker {
    fun runCommand(command: Command) {
        command.execute()
    }
}
