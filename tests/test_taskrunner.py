import unittest

from jnius import autoclass, PythonJavaClass, java_method
import time

class _TaskRunnerCallback(PythonJavaClass):

    __javainterfaces__ = ['org/jnius/TaskRunner$Callback']

    def __init__(self, my_callback):
        self.my_callback = my_callback
        super().__init__()
        
    @java_method('(Lorg/jnius/TaskRunner$TestObject;)V')
    def onComplete(self, result):
        self.my_callback(result)

class TaskRunnerTest(unittest.TestCase):

    def _my_callback(self, result):
        if self.result is None:
            self.result = result

    def test_taskrunner(self):
        self.result = None
        callback = _TaskRunnerCallback(self._my_callback)
        TaskRunner = autoclass("org.jnius.TaskRunner")
        runner = TaskRunner()
        runner.executeAsync(callback)
        time.sleep(.01)
        self.assertNotEqual(self.result.getValue()[0], None)

if __name__ == '__main__':
    unittest.main()