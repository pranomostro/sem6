#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <assert.h>
#include <time.h>

#include "findtest.h"

#define LEN(x) (sizeof (x) / sizeof *(x))

void measure_runtime(uint32_t* data, size_t len, size_t inc, size_t funcpos);

typedef struct
{
	size_t len, maxinc, rounds;
} Findtest;

typedef struct
{
	size_t (*find)(uint32_t key, uint32_t* data, size_t len);
	const char* name, * desc;
} Findfunc;

/* don't touch .len and .maxinc. .rounds can be changed by liking */

Findtest tests[]=
{
	{ .len=1,	.maxinc=1<<28,	.rounds=1<<12	},
	{ .len=2,	.maxinc=1<<28,	.rounds=1<<12	},
	{ .len=3,	.maxinc=1<<28,	.rounds=1<<12	},
	{ .len=4,	.maxinc=1<<28,	.rounds=1<<12	},
	{ .len=1<<4,	.maxinc=1<<28,	.rounds=1<<12	},
	{ .len=1<<8,	.maxinc=1<<24,	.rounds=1<<12	},
	{ .len=1<<12,	.maxinc=1<<20,	.rounds=1<<12	},
	{ .len=1<<16,	.maxinc=1<<16,	.rounds=1<<12	},
	{ .len=1<<20,	.maxinc=1<<12,	.rounds=1<<12	},
	{ .len=1<<24,	.maxinc=1<<8,	.rounds=1<<12	},
	{ .len=1<<28,	.maxinc=1<<4,	.rounds=1<<12	}
};

Findfunc funcs[]=
{
	{ .find=bfind1,	.name="bfind1",	.desc="binary find 1"		},
	{ .find=bfind2,	.name="bfind2",	.desc="binary find 2"		},
	{ .find=efind1,	.name="efind1",	.desc="estimating find 1"	},
	{ .find=efind2,	.name="efind2",	.desc="estimating find 2"	},
	{ .find=ffind1, .name="ffind1", .desc="fast find"		},
	{ .find=ifind1,	.name="ifind1",	.desc="interpolating find 1"	},
	{ .find=ifind2,	.name="ifind2",	.desc="interpolating find 2"	},
	{ .find=qfind1,	.name="qfind1",	.desc="quadratic binary find"	}
};

int measuretime(uint32_t* data, size_t len, size_t inc, size_t funcpos, size_t testpos)
{
	int acc;
	size_t res, i;
	clock_t c1, c2;
	uint32_t key;

	acc=0;

	//printf("%s (len: %lu, inc: %lu): ", funcs[funcpos].name, len, inc);

	for(i=0; i<tests[testpos].rounds; i++)
	{
		key=rand()%(len*inc);
		c1=clock();
		res=funcs[funcpos].find(key, data, len);
		c2=clock();
		acc+=c2-c1;

		assert(res<=len);
		assert(res==len||data[res]>=key);
		assert(res==0||data[res-1]<=key);
	}
	//printf("%d clocks for %lu rounds (%f clocks per round)\n",
	//	acc, tests[testpos].rounds, (float)acc/(float)tests[testpos].rounds);
	return acc;
}

int main(void)
{
	int total;
	size_t i, j, k, n, inc;
	uint32_t* data=calloc(tests[LEN(tests)-1].len+1, sizeof(uint32_t));

	srand((unsigned)time(NULL));

	for(i=0; i<LEN(funcs); i++)
	{
		total=0;
		for(j=0; j<LEN(tests); j++)
			for(inc=tests[j].maxinc; inc>1; inc>>=2)
			{
				for(n=rand()%inc, k=0; k<tests[j].len; k++, n+=rand()%inc)
					data[k]=n;
				total+=measuretime(data, tests[j].len, inc, i, j);
			}
		printf("%s needed %d clocks total\n", funcs[i].name, total);
	}
	free(data);

	return 0;
}
