# GCP README

## install node locally - Ubuntu 20.04

```bash
export NODE_VERSION=node-v10.22.1-linux-x64

wget -P $HOME/Downloads https://nodejs.org/dist/latest-v10.x/$NODE_VERSION.tar.xz

tar -C $HOME/bin/ -xf $HOME/Downloads/$NODE_VERSION.tar.xz

# into ~/.bashrc
export PATH=$HOME/bin/$NODE_VERSION/bin:$PATH
```

## install dependencies

```bash
npm install -g express mocha chai chai-http nyc @google-cloud/storage md5 @google-cloud/firestore markdownlint-cli jsonlint

# update packages.json - in gcp folder
npm update
```

---

## md lint

* markdownlint -c .github/linters/.markdown-lint.yml .

## coverage

* export GOOGLE_APPLICATION_CREDENTIALS=../service-account-01.json
* cd cloud_functions
* nyc mocha --exit

---

## skip a suite / test

prefix describe | it with x to skip - i.e. xit

## curl (but mocha tests have replaced curl)

```javascript
// express
var port = 3000;
app.listen(port, () => console.log(`listening at http://localhost:${port}`));

curl -d '{"code":"012345672e","digests":["d1", "d2"]}' -H "Content-Type: application/json" -X POST http://localhost:3000/save
```

---

## gcp cli init

```bash
echo "deb [signed-by=/usr/share/keyrings/cloud.google.gpg] https://packages.cloud.google.com/apt cloud-sdk main" | sudo tee -a /etc/apt/sources.list.d/google-cloud-sdk.list

sudo apt-get install apt-transport-https ca-certificates gnupg

curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key --keyring /usr/share/keyrings/cloud.google.gpg add -

sudo apt-get update && sudo apt-get install google-cloud-sdk

gcloud init
```

## gcp remote setup (if not using firestore)

* create IAM Service Account: service_account_01
  * assign role: Storage Admin + Storage Object Admin
* create Storage Bucket: favourites_bucket (in us_central1; Fine-grained)

```bash
gcloud auth activate-service-account --key-file=service_account_01.json

gsutil ls -L -b gs://favourites_bucket

gsutil -m rm gs://favourites_bucket/**

gsutil rb gs://favourites_bucket
```

* cloud functions (in us_central1):
  * permissions: allUsers + Cloud Function Invoker

## gcp deploy - uscentral1

```bash
# if changing project
gcloud init
```

---

## cloud_functions

```bash
cd cloud_functions
gcloud functions deploy favourites --runtime nodejs10 --trigger-http

# with permissions: allUsers + Cloud Function Invoker

curl -d '{"code":"0123456789","digests":["d1", "d2"]}' -H "Content-Type: application/json" -X POST https://us-central1-neat-episode-281012.cloudfunctions.net/favourites/save

curl -d '{"code":"0123456789"}' -H "Content-Type: application/json" -X POST https://us-central1-neat-episode-281012.cloudfunctions.net/favourites/receive

```

### cloud_scheduler (if not using firestore)

```bash
cd cloud_scheduler
gcloud functions deploy favourites_cleaner --runtime nodejs10 --trigger-http

# with permissions: allUsers + Cloud Function Invoker

# neat-episode-281012@appspot.gserviceaccount.com role of "Storage Object Admin" to bucket
```

### config the gcp cloud scheduler

```text
favourites_cleaner
59 23 * * *
United Kingdom
HTTP
https://us-central1-neat-episode-281012.cloudfunctions.net/favourites_cleaner
GET
```

---

## firestore - eur3

```text
Cloud Firestore in Native mode
```

---

## useful links

* <https://www.w3schools.com/js>

* <https://nodejs.org/en/knowledge/getting-started/control-flow/what-are-callbacks/>

* <https://www.npmjs.com/package/@google-cloud>

* <https://googleapis.dev/nodejs/storage/latest>

* <https://github.com/googleapis/nodejs-firestore>
