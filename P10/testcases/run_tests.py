import os
import sys
import filecmp

test_cases = {
	100: ['100_1', '100_2', '100_3'],
	200: ['200_1', '200_2', '200_3'],
	300: ['300_1', '300_2', '300_3'],
	400: ['400_1', '400_2', '400_3'],
	500: ['500'],
	600: ['600'],
}

def run_all():
	passed = True
	for part in test_cases:
		passed = run(part) and passed

	if passed:
		print()
		print('All sample tests passed.')
		print('REMEMBER: This doesn\'t guarantee that your program is correct in all the scenarios.')


def run(part):
	passed = True

	for test_case in test_cases[part]:
		if os.path.isfile(test_case + '.out'):
			if not filecmp.cmp(test_case + '.exp', test_case + '.out', shallow=False):
				passed = False
				print('Test case', test_case, 'failed')
		else:
			print('Plese provide sample output for', test_case)
			passed = False

	if passed:
		print('Tests passed for part', part)
	
	return passed

if __name__ == "__main__":
	if len(sys.argv) == 1:
		run_all()
	else:
		for part in sys.argv[1:]:
			run(int(part))
