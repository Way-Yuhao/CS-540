import os
import sys
import filecmp

test_cases = {
	100: ['100_0', '100_1', '100_2000', '100_3001', '100_4699'],
	200: ['200_32_1000', '200_321_10000', '200_5000_10000', '200_99997_100000'],
	300: ['300_414_2297', '300_0_0', '300_0_1', '300_2110_4240', '300_4247_0'],
	400: ['400_0_100_414', '400_1_100_414', '400_98_100_414', '400_81_100_4697', '400_15_100_4442'],
	500: ['500_23_12_123', '500_5_660_3425', '500_2799_556_2364', '500_414_2297_2364', '500_0_0_0'],
	600: ['600_2_5_660_3425', '600_2_5_3001_104', '600_50_100_496_4517', '600_33_100_2591_2473', '600_0_100_2297_414', '600_0_100_496_4517'],
	700: ['700_0_0', '700_0_1_414', '700_1_1_523', '700_31_2_2110_311']
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
