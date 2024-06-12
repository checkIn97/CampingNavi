import pandas as pd
from surprise import SVD
from surprise import Dataset
from surprise import Reader
from surprise.model_selection import GridSearchCV
from surprise.dataset import DatasetAutoFolds
import joblib
import os
import warnings; warnings.filterwarnings('ignore')

# 초기 평점 데이터
rating_init_file = 'ratingList.csv'
rating_init_raw = pd.read_csv(rating_init_file, encoding='utf-8', sep=';')
rating_init = rating_init_raw[['회원', '캠핑장ID', '평점']]
rating_init.rename(columns={'회원':'member', '캠핑장ID':'camp', '평점':'rate'}, inplace=True)
rating_init.dropna(inplace=True)
rating_init['member'] = rating_init['member'].apply(lambda x: 'm'+str(hash(x)))


# 새로 축적된 리뷰 데이터 (평점 포함)
rating_new_file = 'Review.csv'
rating_new = pd.read_csv(rating_new_file, encoding='utf-8', sep=',')

# 평점 데이터 통합
rating = pd.concat([rating_init, rating_new])
rating.reset_index(inplace=True)
rating.drop('index', axis=1, inplace=True)

# camp번호 규격 정리
def refine_camp(x):
    try:
        x = str(x)[:-2]
    except:
        pass
    return x

# 평점 규격 정리
def refine_rate(x):
    try:
        x = str(x)
    except:
        pass
    index = x.find('/')
    if index != -1:
        x = x[:index]
    return float(x)


rating['camp'] = rating['camp'].apply(refine_camp)
rating['rate'] = rating['rate'].apply(refine_rate)


reader = Reader(rating_scale=(0.0, 5.0))
data = Dataset.load_from_df(rating[['member', 'camp', 'rate']], reader)

# 최적화할 파라미터
param_grid = {'n_epochs':[20, 40, 60, 80, 100], 'n_factors':[50, 100, 150, 200]}

# CV를 3개의 폴드로 지정하고 rmse, mse 로 평가 수행
gs = GridSearchCV(SVD, param_grid, measures=['rmse', 'mse'], cv=3)
gs.fit(data)

best_epochs = gs.best_params['rmse']['n_epochs']
best_factors = gs.best_params['rmse']['n_factors']

# 최적화 파라미터로 학습 수행
rating_noh = rating.copy()
rating_noh_file = 'rating_noh.csv'
rating_noh.to_csv(rating_noh_file, sep=';', encoding='utf-8-sig', index=False, header=False)
reader = Reader(line_format='user item rating', sep=';', rating_scale=(0.5, 5.0))
data_folds = DatasetAutoFolds(ratings_file=rating_noh_file, reader=reader)

# 전체 데이터를 학습데이터로 생성
trainset = data_folds.build_full_trainset()

# 학습
svd = SVD(n_epochs=best_epochs, n_factors=best_factors, random_state=0)
svd.fit(trainset)

# 학습 결과를 저장하고 임시파일 삭제
model_file = 'model.pkl'
joblib.dump(svd, model_file)

while True:
    if os.path.exists(model_file):
        break

if os.path.exists(rating_noh_file):
    os.remove(rating_noh_file)

print('success')